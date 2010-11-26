package fm.audiobox.core.models;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.AudioBox;
import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.ModelException;
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
	
}
