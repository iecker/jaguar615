/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.jsf.validator;

import java.security.AccessController;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.MessageInterpolator;

import org.apache.log4j.Logger;
import org.hibernate.validator.util.privilegedactions.GetClassLoader;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;


/**
 * Resource bundle backed message interpolator.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class PlcResourceBundleMessageInterpolator implements MessageInterpolator {
	private static final String DEFAULT_VALIDATION_MESSAGES = "jCompanyResources";
	private static final String USER_VALIDATION_MESSAGES = "ApplicationResources";
	private static final Logger log = Logger.getLogger(PlcResourceBundleMessageInterpolator.class.getCanonicalName());

	/**
	 * Regular expression used to do message interpolation.
	 */
	private static final Pattern messageParameterPattern = Pattern.compile( "(\\{[^\\}]+?\\})" );

	/**
	 * The default locale for the current user.
	 */
	private final Locale defaultLocale;

	/**
	 * User specified resource bundles hashed against their locale.
	 */
	private final Map<Locale, ResourceBundle> userBundlesMap = new ConcurrentHashMap<Locale, ResourceBundle>();

	/**
	 * Built-in resource bundles hashed against there locale.
	 */
	private final Map<Locale, ResourceBundle> defaultBundlesMap = new ConcurrentHashMap<Locale, ResourceBundle>();

	/**
	 * Step 1-3 of message interpolation can be cached. We do this in this map.
	 */
	private final Map<LocalisedMessage, String> resolvedMessages = new WeakHashMap<LocalisedMessage, String>();

	public PlcResourceBundleMessageInterpolator() {
		this( null );
	}

	public PlcResourceBundleMessageInterpolator(ResourceBundle resourceBundle) {
		
		PlcI18nUtil i18nUtil = null;
		
		Locale _defaultLocale=null;
		try	{
			i18nUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcI18nUtil.class, QPlcDefaultLiteral.INSTANCE);
			if (i18nUtil != null && i18nUtil.getCurrentLocale() != null){
				_defaultLocale = i18nUtil.getCurrentLocale();
			}else{
				_defaultLocale = Locale.getDefault();
			}
		} catch (Exception e) {
			_defaultLocale = Locale.getDefault();
		}
		defaultLocale = _defaultLocale;


		if ( resourceBundle == null ) {
			ResourceBundle bundle = getFileBasedResourceBundle( defaultLocale );
			if ( bundle != null ) {
				userBundlesMap.put( defaultLocale, bundle );
			}

		}
		else {
			userBundlesMap.put( defaultLocale, resourceBundle );
		}

		defaultBundlesMap.put( defaultLocale, ResourceBundle.getBundle( DEFAULT_VALIDATION_MESSAGES, defaultLocale ) );
	}

	public String interpolate(String message, Context context) {
		// probably no need for caching, but it could be done by parameters since the map
		// is immutable and uniquely built per Validation definition, the comparison has to be based on == and not equals though
		return interpolateMessage( message, context.getConstraintDescriptor().getAttributes(), defaultLocale );
	}

	public String interpolate(String message, Context context, Locale locale) {
		return interpolateMessage( message, context.getConstraintDescriptor().getAttributes(), locale );
	}
	
	public String interpolate(String message, Map<String, Object> annotationParameters, Locale locale) {
		return interpolateMessage( message, annotationParameters, locale );
	}

	/**
	 * Runs the message interpolation according to algorithm specified in JSR 303.
	 * <br/>
	 * Note:
	 * <br/>
	 * Look-ups in user bundles is recursive whereas look-ups in default bundle are not!
	 *
	 * @param message the message to interpolate
	 * @param annotationParameters the parameters of the annotation for which to interpolate this message
	 * @param locale the <code>Locale</code> to use for the resource bundle.
	 *
	 * @return the interpolated message.
	 */
	private String interpolateMessage(String message, Map<String, Object> annotationParameters, Locale locale) {
		
		LocalisedMessage localisedMessage = new LocalisedMessage( message, locale );
		
		String resolvedMessage = resolvedMessages.get( localisedMessage );

		// if the message is not already in the cache we have to run step 1-3 of the message resolution 
		if ( resolvedMessage == null ) {
			ResourceBundle userResourceBundle = findUserResourceBundle( locale );
			ResourceBundle defaultResourceBundle = findDefaultResourceBundle( locale );

			String userBundleResolvedMessage;
			resolvedMessage = message;
			boolean evaluatedDefaultBundleOnce = false;
			do {
				// search the user bundle recursive (step1)
				userBundleResolvedMessage = replaceVariables(
						resolvedMessage, userResourceBundle, locale, true
				);

				// exit condition - we have at least tried to validate against the default bundle and there was no
				// further replacements
				if ( evaluatedDefaultBundleOnce
						&& !hasReplacementTakenPlace( userBundleResolvedMessage, resolvedMessage ) ) {
					break;
				}

				// search the default bundle non recursive (step2)
				resolvedMessage = replaceVariables( userBundleResolvedMessage, defaultResourceBundle, locale, false );
				evaluatedDefaultBundleOnce = true;
				resolvedMessages.put( localisedMessage, resolvedMessage );
			} while ( true );
		}

		// resolve annotation attributes (step 4)
		resolvedMessage = replaceAnnotationAttributes( resolvedMessage, annotationParameters );

		// last but not least we have to take care of escaped literals
		resolvedMessage = resolvedMessage.replace( "\\{", "{" );
		resolvedMessage = resolvedMessage.replace( "\\}", "}" );
		resolvedMessage = resolvedMessage.replace( "\\\\", "\\" );

		if (resolvedMessage.equals(message) && message.startsWith("{") && message.endsWith("}")){
			resolvedMessage = "??? " + locale + " " + resolvedMessage.replace("{", "").replace("}", "") + " ???";
		}
		
		return resolvedMessage;
	}

	private boolean hasReplacementTakenPlace(String origMessage, String newMessage) {
		return !origMessage.equals( newMessage );
	}

	/**
	 * Search current thread classloader for the resource bundle. If not found, search validator (this) classloader.
	 *
	 * @param locale The locale of the bundle to load.
	 *
	 * @return the resource bundle or <code>null</code> if none is found.
	 */
	private ResourceBundle getFileBasedResourceBundle(Locale locale) {
		
		ResourceBundle rb = null;
		
		boolean isSecured = System.getSecurityManager() != null;
		
		GetClassLoader action = GetClassLoader.fromContext();
		
		ClassLoader classLoader = isSecured ? AccessController.doPrivileged( action ) : action.run();

		if ( classLoader != null ) {
			rb = loadBundle( classLoader, locale, USER_VALIDATION_MESSAGES + " not found by thread local classloader" );
		}
		if ( rb == null ) {
			action = GetClassLoader.fromClass( PlcResourceBundleMessageInterpolator.class );
			classLoader = isSecured ? AccessController.doPrivileged( action ) : action.run();
			rb = loadBundle(
					classLoader,
					locale,
					USER_VALIDATION_MESSAGES + " not found by validator classloader"
			);
		}
		if ( log.isDebugEnabled() ) {
			if ( rb != null ) {
				log.debug(  USER_VALIDATION_MESSAGES + " found" );
			}
			else {
				log.debug(  USER_VALIDATION_MESSAGES + " not found. Delegating to " + DEFAULT_VALIDATION_MESSAGES );
			}
		}
		return rb;
	}

	private ResourceBundle loadBundle(ClassLoader classLoader, Locale locale, String message) {
		
		ResourceBundle rb = null;
		
		try {
			rb = ResourceBundle.getBundle( USER_VALIDATION_MESSAGES, locale, classLoader );
		}
		catch ( MissingResourceException e ) {
			log.error(  message );
		}
		return rb;
	}

	private String replaceVariables(String message, ResourceBundle bundle, Locale locale, boolean recurse) {
		
		Matcher matcher = messageParameterPattern.matcher( message );
		StringBuffer sb = new StringBuffer();
		String resolvedParameterValue;
		
		while ( matcher.find() ) {
			String parameter = matcher.group( 1 );
			resolvedParameterValue = resolveParameter(
					parameter, bundle, locale, recurse
			);

			matcher.appendReplacement( sb, escapeMetaCharacters( resolvedParameterValue ) );
		}
		matcher.appendTail( sb );
		return sb.toString();
	}

	private String replaceAnnotationAttributes(String message, Map<String, Object> annotationParameters) {
		
		Matcher matcher = messageParameterPattern.matcher( message );
		StringBuffer sb = new StringBuffer();
		
		while ( matcher.find() ) {
			String resolvedParameterValue;
			String parameter = matcher.group( 1 );
			Object variable = annotationParameters.get( removeCurlyBrace( parameter ) );
			if ( variable != null ) {
				resolvedParameterValue = escapeMetaCharacters( variable.toString() );
			}
			else {
				resolvedParameterValue = parameter;
			}
			matcher.appendReplacement( sb, resolvedParameterValue );
		}
		matcher.appendTail( sb );
		return sb.toString();
	}

	private String resolveParameter(String parameterName, ResourceBundle bundle, Locale locale, boolean recurse) {
		
		String parameterValue;
		try {
			if ( bundle != null ) {
				parameterValue = bundle.getString( removeCurlyBrace( parameterName ) );
				if ( recurse ) {
					parameterValue = replaceVariables( parameterValue, bundle, locale, recurse );
				}
			}
			else {
				parameterValue = parameterName;
			}
		}
		catch ( MissingResourceException e ) {
			// return parameter itself
			parameterValue = parameterName;
		}
		return parameterValue;
	}

	private String removeCurlyBrace(String parameter) {
		return parameter.substring( 1, parameter.length() - 1 );
	}

	private ResourceBundle findDefaultResourceBundle(Locale locale) {
		if ( defaultBundlesMap.containsKey( locale ) ) {
			return defaultBundlesMap.get( locale );
		}

		ResourceBundle bundle = ResourceBundle.getBundle( DEFAULT_VALIDATION_MESSAGES, locale );
		defaultBundlesMap.put( locale, bundle );
		return bundle;
	}

	private ResourceBundle findUserResourceBundle(Locale locale) {
		if ( userBundlesMap.containsKey( locale ) ) {
			return userBundlesMap.get( locale );
		}

		ResourceBundle bundle = getFileBasedResourceBundle( locale );
		if ( bundle != null ) {
			userBundlesMap.put( locale, bundle );
		}
		return bundle;
	}

	/**
	 * @param s The string in which to replace the meta characters '$' and '\'.
	 *
	 * @return A string where meta characters relevant for {@link Matcher#appendReplacement} are escaped.
	 */
	private String escapeMetaCharacters(String s) {
		String escapedString = s.replace( "\\", "\\\\" );
		escapedString = escapedString.replace( "$", "\\$" );
		return escapedString;
	}

	private static class LocalisedMessage {
		private final String message;
		private final Locale locale;

		LocalisedMessage(String message, Locale locale) {
			this.message = message;
			this.locale = locale;
		}

		public String getMessage() {
			return message;
		}

		public Locale getLocale() {
			return locale;
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			LocalisedMessage that = ( LocalisedMessage ) o;

			if ( locale != null ? !locale.equals( that.locale ) : that.locale != null ) {
				return false;
			}
			if ( message != null ? !message.equals( that.message ) : that.message != null ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = message != null ? message.hashCode() : 0;
			result = 31 * result + ( locale != null ? locale.hashCode() : 0 );
			return result;
		}
	}
}
