<?cs def:custom_masthead() ?>
  <div id="header">
    <div id="logo">
      <img src="<?cs var:toroot ?>../assets/images/logo.png" alt="audiobox" />
    </div>
    <div id="headerLeft">
      <?cs if:project.name ?>
        <span id="masthead-title"><?cs var:project.name ?></span>
      <?cs /if ?>
    </div>
    <div id="headerRight">
      <?cs call:default_search_box() ?>
      <?cs if:reference && reference.apilevels ?>
        <?cs call:default_api_filter() ?>
      <?cs /if ?>
    </div>
  </div><!-- header -->
<?cs /def ?>
