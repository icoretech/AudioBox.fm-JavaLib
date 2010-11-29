package fm.audiobox.core.models;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fm.audiobox.core.AudioBox.Utils;
import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.observers.Listener;
import fm.audiobox.core.util.Inflector;

public class ModelFactory {
	
	private static Logger log = LoggerFactory.getLogger(ModelFactory.class);
	
	/** Specifies the models package (default: fm.audiobox.core.models) */
    public static final String DEFAULT_MODELS_PACKAGE = ModelFactory.class.getPackage().getName();

    /** Constant <code>USER_KEY="User.TAG_NAME"</code> */
    public static final String USER_KEY      = User.TAG_NAME;

    /** Constant <code>PROFILE_KEY="Profile.TAG_NAME"</code> */
    public static final String PROFILE_KEY   = Profile.TAG_NAME;

    /** Constant <code>PLAYLISTS_KEY="Playlists"</code> */
    public static final String PLAYLISTS_KEY = "Playlists";

    /** Constant <code>PLAYLIST_KEY="Playlist.TAG_NAME"</code> */
    public static final String PLAYLIST_KEY  = Playlist.TAG_NAME;

    /** Constant <code>GENRES_KEY="Genres"</code> */
    public static final String GENRES_KEY    = "Genres";

    /** Constant <code>GENRE_KEY="Genre.TAG_NAME"</code> */
    public static final String GENRE_KEY     = Genre.TAG_NAME;

    /** Constant <code>ARTISTS_KEY="Artists"</code> */
    public static final String ARTISTS_KEY   = "Artists";

    /** Constant <code>ARTIST_KEY="Artist.TAG_NAME"</code> */
    public static final String ARTIST_KEY    = Artist.TAG_NAME;

    /** Constant <code>ALBUMS_KEY="Albums"</code> */
    public static final String ALBUMS_KEY    = "Albums";

    /** Constant <code>ALBUM_KEY="Album.TAG_NAME"</code> */
    public static final String ALBUM_KEY     = Album.TAG_NAME;

    /** Constant <code>TRACKS_KEY="Tracks"</code> */
    public static final String TRACKS_KEY    = "Tracks";
    
    /** Constant <code>TRACK_KEY="Track.TAG_NAME"</code> */
    public static final String TRACK_KEY     = Track.TAG_NAME;
    
    /** Constant <code>ERROR_KEY="Tracks"</code> */
    public static final String ERROR_KEY     = Error.TAG_NAME;

    /** Constant <code>NEW_TRACK_KEY="Track.TAG_NAME"</code> */
    public static final String NEW_TRACK_KEY     = "NewTrack";
    
    private static Inflector sI = Inflector.getInstance();
	
    /** Model classes collection */
	private Map<String, Class<? extends Model>> mModelsMap;
	
	private Map<String, List<Listener>> mListenersMap;
    
	
	private static Map<String, String> mShortFieldsMap = new HashMap<String, String>();
	static {
		mShortFieldsMap.put("tk", "token");
		mShortFieldsMap.put("t", "title");
		mShortFieldsMap.put("d", "duration");
		mShortFieldsMap.put("ds", "duration_in_seconds");
		mShortFieldsMap.put("su", "stream_url");
		mShortFieldsMap.put("y", "year");
		mShortFieldsMap.put("l", "loved");
		mShortFieldsMap.put("pc", "play_count");
		mShortFieldsMap.put("fs", "audio_file_size");
		mShortFieldsMap.put("tn", "track_number");
		mShortFieldsMap.put("dn", "disc_number");
		mShortFieldsMap.put("fn", "original_file_name");
		mShortFieldsMap.put("al", "album");
		mShortFieldsMap.put("ar", "artist");
	}
	
	
	public ModelFactory() {
		mModelsMap = new HashMap<String , Class<? extends Model>>();
		mModelsMap.put( USER_KEY,      User.class ); 
		mModelsMap.put( PROFILE_KEY ,  Profile.class );
		mModelsMap.put( PLAYLISTS_KEY, Playlists.class ); 
        mModelsMap.put( PLAYLIST_KEY,  Playlist.class );
        mModelsMap.put( GENRES_KEY,    Genres.class ); 
        mModelsMap.put( GENRE_KEY,     Genre.class );
        mModelsMap.put( ARTISTS_KEY,   Artists.class ); 
        mModelsMap.put( ARTIST_KEY,    Artist.class );
        mModelsMap.put( ALBUMS_KEY,    Albums.class ); 
        mModelsMap.put( ALBUM_KEY ,    Album.class );
        mModelsMap.put( TRACKS_KEY,    Tracks.class ); 
        mModelsMap.put( TRACK_KEY ,    Track.class );
        mModelsMap.put( NEW_TRACK_KEY , Track.class );
        mModelsMap.put( ERROR_KEY,      Error.class );
        
        mListenersMap = new HashMap<String, List<Listener>>();
    }
	
	/**
     * <p>If you need to customize or extend the default models classes you can set your own implementation through
     * this method.</p>
     * 
     * @param key one of the key defined as AudioBoxClient model constants,
     * @param klass your extended {@link Model} {@link Class}.
     */
	public void setModelClassFor(String key, Class<? extends Model> klass){
		mModelsMap.put( key , klass);
	}
	
	/**
     * <p>Create new {@link Model} object based upon the provided key.</p>
     *
     * @param key one of the key defined as AudioBoxClient model constants.
     * @param connector CollectionListener implementation or null.
     * 
     * @return a {@link Model} object.
     * 
     * @throws ModelException if provided key isn't covered from the models map.
     */
    @SuppressWarnings("unchecked")
    public Model getModelInstance(String key) throws ModelException {

        Model model = null;
        Class<? extends Model> klass = this.mModelsMap.get( key );

        if ( klass == null ) {
            String className = DEFAULT_MODELS_PACKAGE + "." + sI.upperCamelCase( key, '-' );

            try {
                klass = (Class<? extends Model>) Class.forName( className );
                this.setModelClassFor( key, klass ); // Reset key
            } catch (ClassNotFoundException e) {
                throw new ModelException("No model class found: " + className, ModelException.CLASS_NOT_FOUND );
            }
        }

        try {

            log.trace("New model instance: " + klass.getName() );
            model = klass.newInstance();

        } catch (InstantiationException e) {
            throw new ModelException("Instantiation Exception: " + klass.getName(), ModelException.INSTANTIATION_FAILED );

        } catch (IllegalAccessException e) {
            throw new ModelException("Illegal Access Exception: " + klass.getName(), ModelException.ILLEGAL_ACCESS );

        }

        return model;
    }
	
    
    public void addListenerFor(String key, Listener listener){
    	List<Listener> listeners = this.getListeners(key);
    	listeners.add(listener);
    }
    
    
    public List<Listener> getListeners(){
    	List<Listener> listeners = new ArrayList<Listener>();
    	Iterator<String> keys = this.mListenersMap.keySet().iterator();
    	while( keys.hasNext() )
    		listeners.addAll( this.getListeners(keys.next() ) );
    	return listeners;
    }
    
    public List<Listener> getListeners(String key){
    	List<Listener> listeners = this.mListenersMap.get(key);
    	if ( listeners == null ){
    		listeners = new ArrayList<Listener>();
    		this.mListenersMap.put(key, listeners);
    	}
    	return listeners;
    }
    
    
    public ModelParser getModelParser(Model model, Utils utils){
    	return new ModelParser(model,utils);
    }    

    public class ModelParser extends DefaultHandler {
    	
    	private static final String ADD_PREFIX = "add";
        private static final String SET_PREFIX = "set";
    	
    	private Model model;
    	private Utils utils;
    	private Stack<Object> mStack;
    	private boolean mSkipField;
    	private StringBuffer mStringBuffer = new StringBuffer();
    	
    	private ModelParser(Model model, Utils utils){
    		this.model = model;
    		this.utils = utils;
    	}
    	
    	
    	/** {@inheritDoc} */
        @Override
        public final void startDocument() throws SAXException {
            this.mStack = new Stack<Object>();
            this.mStack.push( this.model );
            super.startDocument();
        }

        /** {@inheritDoc} */
        @Override
        public void endDocument() throws SAXException {
            this.mStack = null;
            super.endDocument();
        }


        /** {@inheritDoc} */
        @Override
        public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            Object peek = this.mStack.peek();

            mSkipField = false;

            try {
                if (localName.trim().length() == 0)
                    localName = qName;
                
                if ( mShortFieldsMap.containsKey(localName) )
                	localName = mShortFieldsMap.get(localName);

                localName =  sI.upperCamelCase( localName, '_' );

                String methodPrefix = SET_PREFIX;
                String scm = sI.singularize( peek.getClass().getSimpleName() );
                if (!scm.equals( peek.getClass().getSimpleName() ) && localName.equals( scm  ) ) {
                    methodPrefix = ADD_PREFIX;
                }

                String methodName =  methodPrefix + localName;
                Method method = null;
                try {
                    method = peek.getClass().getMethod(methodName, String.class);
                } catch (NoSuchMethodException e) {
                    for (Method m : peek.getClass().getMethods()) {
                        if (m.getName().equals( methodName )) {
                            method = m;
                            break;
                        }
                    }
                }


                if (method == null)
                    mSkipField = true;

                if ( !mSkipField ) {

                    Class<?> argType = method.getParameterTypes()[0];

                    if ( ! argType.isPrimitive() && ! argType.equals(String.class) ){

                        Model subClass = null;
                        try {
                            subClass = this.utils.getModelInstance( sI.lowerCamelCase( localName, '_') );
                            method.invoke(peek, subClass );

                            this.mStack.push( subClass );
                        } catch (ModelException e) {
                            e.printStackTrace();
                        }

                    } else {
                        this.mStack.push( method );
                    }
                }

            } catch (IllegalArgumentException e) {
                log.error("Illegal Argument Exception @" + localName + ": " + e.getMessage());
                e.printStackTrace();

            } catch (IllegalAccessException e) {
                log.error("Illegal AccessException @" + localName + ": " + e.getMessage());
                e.printStackTrace();

            } catch (SecurityException e) {
                log.error("Security Exception @" + localName + ": " + e.getMessage());
                e.printStackTrace();

            } catch (InvocationTargetException e) {
                log.error("Invocation Target Exception @" + localName + ": " + e.getMessage());
                e.printStackTrace();

            } 

            super.startElement(uri, localName, qName, attributes);
        }

        /** {@inheritDoc} */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            if ( !mSkipField ) {

                String _temp = mStringBuffer.toString();
                _temp = _temp.replace("\n","").trim();
                mStringBuffer = new StringBuffer();


                if (this.mStack.peek() instanceof Method) {

                    Method method = ( Method ) this.mStack.peek();
                    Object _dest = this.mStack.get(  this.mStack.size() - 2 );

                    try {
                        if (_temp.trim().length() > 0) {
                            method.invoke(_dest, _temp);
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }

                this.mStack.pop();

            }

            super.endElement(uri, localName, qName);
        }

        /** {@inheritDoc} */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

            if ( !mSkipField ) {
                mStringBuffer.append( String.valueOf( ch , start , length ) );
            }

            super.characters(ch, start, length);
        }
    	
    	
    	
    }
    
    
}
