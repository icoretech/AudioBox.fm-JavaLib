package fm.audiobox.core.parsers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;


/**
 * This is an utility class used for parsing a XML string.
 * <br />
 * It also populates the {@link IEntity} associated with this instance
 */
@Deprecated
public class XmlParser extends DefaultHandler {

  private static Logger log = LoggerFactory.getLogger(XmlParser.class);

  private long startParse = 0;

  private IEntity entity;

  private IConfiguration config;

  private Stack<IEntity> stack;

  private StringBuffer bodyContent;

  public XmlParser(IEntity entity) {
    this.entity = entity;
    this.config = entity.getConfiguration();
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    if (this.bodyContent != null)
      this.bodyContent.append(new String(ch, start, length));
    super.characters(ch, start, length);
  }

  public void startDocument() throws SAXException {
    this.stack = new Stack<IEntity>();
    this.stack.push(this.entity);
    if (log.isDebugEnabled()) {
      startParse = System.currentTimeMillis();
    }
    super.startDocument();
  }

  public void endDocument() throws SAXException {
    this.stack.clear();
    this.stack = null;
    this.bodyContent = null;
    if (log.isDebugEnabled()) {
      log.debug("Document parsed in " + (System.currentTimeMillis() - startParse) + "ms (" + this.entity.getNamespace() + ")");
    }
    super.endDocument();
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    if ("".equals(localName.trim())) {
      localName = qName;
    }

    if (localName.matches(this.stack.peek().getTagName())) {
      // Start tag must be skipped
      return;
    }

    if (this.config.getFactory().containsEntity(localName)) {

      IEntity newEntity = this.config.getFactory().getEntity(localName, this.config);
      this.stack.push(newEntity);

      if (log.isTraceEnabled()) {
        log.trace("New Entity '" + newEntity, getClass().getName() + "' for tag: " + localName);
      }

    } else {

      this.bodyContent = new StringBuffer("");

    }

  }

  public void endElement(String uri, String localName, String qName) throws SAXException {

    if ("".equals(localName.trim())) {
      localName = qName;
    }

    // get the Entity from stack
    IEntity currentEntity = this.stack.peek();
    IEntity newEntity = null;

    if (localName.matches(currentEntity.getTagName())) {
      // end element for current entity
      log.trace("EndElement reached for tag: " + localName);
      newEntity = this.stack.pop();
      if (this.stack.size() == 0) {
        if (localName.matches(this.entity.getTagName())) {
          // XML parsed completely
          return;
        } else {
          log.warn("*** XML wasn't parsed correctly ***");
          return;
        }
      }
      currentEntity = this.stack.peek();
    }

    // FIX: remove uneeded characters
    String value = "";
    if (this.bodyContent != null) {
      value = this.bodyContent.toString().replaceAll("\\n|\\r|\\t", "").trim();
      if (log.isTraceEnabled()) {
        log.trace("Body content: " + value);
      }
    }

    if (currentEntity != null) {

      Method setterMethod = null;
      try {
        setterMethod = currentEntity.getSetterMethod(localName);
      } catch (SecurityException e) {
        log.error("No accessible method found under key: " + localName, e);
        return;
      }

      if (setterMethod == null) {
        log.warn(currentEntity.getClass().getName() + " doesn't contain the request method for tag: " + localName);
        return;
      }

      // Setter method found!
      if (setterMethod.getParameterTypes().length == 1) {

        Class<?> paramType = setterMethod.getParameterTypes()[0];

        try {
          /*
           * Calculating the method parameters
           */

          if (paramType.equals(int.class)) {
            /*
             * FIX: if value is an empty string the Integer.parseInt method
             * fails. To prevent errors we set value to zero as string
             */
            value = "".equals(value) ? "0" : value;
            setterMethod.invoke(currentEntity, Integer.parseInt(value));

          } else if (paramType.equals(long.class)) {
            /*
             * FIX: if value is an empty string the Long.parseLong method fails.
             * To prevent errors we set value to zero as string
             */
            value = "".equals(value) ? "0" : value;
            setterMethod.invoke(currentEntity, Long.parseLong(value));

          } else if (paramType.equals(boolean.class)) {

            setterMethod.invoke(currentEntity, Boolean.parseBoolean(value));

          } else if (paramType.equals(String.class)) {

            setterMethod.invoke(currentEntity, value);

          } else {
            /*
             * In this case we have to check if method parameter is an IEntity
             */
            boolean isEntity = false;
            try {
              isEntity = paramType.asSubclass(IEntity.class) != null;
            } catch (ClassCastException e) {
              ; // silent fail
            }

            if (isEntity) {
              /*
               * Method parameter seems to be an IEntity. We invoke method
               * passing current IEntity
               */
              setterMethod.invoke(currentEntity, newEntity);
            }

          }

        } catch (NumberFormatException e) {
          // An error occurred while parsing String
          if (log.isDebugEnabled()) {
            log.info("Method cannot be invoked for tag: " + localName, e);
          } else {
            log.info("Method cannot be invoked for tag: " + localName);
          }
        } catch (IllegalArgumentException e) {
          log.error("An error while invoking method '" + setterMethod + "' for tag: " + localName, e);
        } catch (IllegalAccessException e) {
          log.error("An error while invoking method '" + setterMethod + "' for tag: " + localName, e);
        } catch (InvocationTargetException e) {
          log.error("An error while invoking method '" + setterMethod + "' for tag: " + localName, e);
        }

      }

    } else {

      // An error might have occurred
      // do nothing
      log.debug("An error might have occurred while parsing tag: " + localName);

    }

    // blank bodyContent
    this.bodyContent = null;
    super.endElement(uri, localName, qName);
  }

}
