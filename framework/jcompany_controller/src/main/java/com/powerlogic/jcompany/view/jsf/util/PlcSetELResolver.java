/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.util;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

public class PlcSetELResolver extends ELResolver {

    /**
     * Creates a new read/write <code>ListELResolver</code>.
     */
    public PlcSetELResolver() {
        this.isReadOnly = false;
    }

    /**
     * Creates a new <code>ListELResolver</code> whose read-only status is
     * determined by the given parameter.
     *
     * @param isReadOnly <code>true</code> if this resolver cannot modify
     *     lists; <code>false</code> otherwise.
     */
    public PlcSetELResolver(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * If the base object is a list, returns the most general acceptable type 
     * for a value in this list.
     *
     * <p>If the base is a <code>List</code>, the <code>propertyResolved</code>
     * property of the <code>ELContext</code> object must be set to
     * <code>true</code> by this resolver, before returning. If this property
     * is not <code>true</code> after this method is called, the caller 
     * should ignore the return value.</p>
     *
     * <p>Assuming the base is a <code>List</code>, this method will always
     * return <code>Object.class</code>. This is because <code>List</code>s
     * accept any object as an element.</p>
     *
     * @param context The context of this evaluation.
     * @param base The list to analyze. Only bases of type <code>List</code>
     *     are handled by this resolver.
     * @param property The index of the element in the list to return the 
     *     acceptable type for. Will be coerced into an integer, but 
     *     otherwise ignored by this resolver.
     * @return If the <code>propertyResolved</code> property of 
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     the most general acceptable type; otherwise undefined.
     * @throws PropertyNotFoundException if the given index is out of 
     *     bounds for this list.
     * @throws NullPointerException if context is <code>null</code>
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    public Class<?> getType(ELContext context,
                         Object base,
                         Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Set) {
            context.setPropertyResolved(true);
            Set set = (Set) base;
            int index = toInteger(property);
            if (index < 0 || index >= set.size()) {
                throw new PropertyNotFoundException();
            } 
            return Object.class;
        }
        return null;
    }


    public Object getValue(ELContext context,
                           Object base,
                           Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Set) {
            context.setPropertyResolved(true);
            Set set = (Set) base;
            int index = toInteger(property);
            if (index < 0 || index >= set.size()) {
                return null;
            } 
            int cont = 0;
            for (Object object : set) {
		if (cont==index) 
	            return object;		    
		cont++;
	    }
            
        }
        return null;
    }

    public void setValue(ELContext context,
                         Object base,
                         Object property,
                         Object val) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Set) {
            context.setPropertyResolved(true);
            Set set = (Set) base;
            if (isReadOnly) {
                throw new PropertyNotWritableException();
            }
            try {
                set.remove(getValue(context, base, property));
        	set.add(val);
                
            } catch (UnsupportedOperationException ex) {
                throw new PropertyNotWritableException();
            } catch (IndexOutOfBoundsException ex) {
                throw new PropertyNotFoundException();
            } catch (ClassCastException ex) {
                throw ex;
            } catch (NullPointerException ex) {
                throw ex;
            } catch (IllegalArgumentException ex) {
                throw ex;
            }
        }
    }

    static private Class<?> theUnmodifiableSetClass =
        Collections.unmodifiableSet(new HashSet()).getClass();

    /**
     * If the base object is a list, returns whether a call to 
     * {@link #setValue} will always fail.
     *
     * <p>If the base is a <code>List</code>, the <code>propertyResolved</code>
     * property of the <code>ELContext</code> object must be set to
     * <code>true</code> by this resolver, before returning. If this property
     * is not <code>true</code> after this method is called, the caller 
     * should ignore the return value.</p>
     *
     * <p>If this resolver was constructed in read-only mode, this method will
     * always return <code>true</code>.</p>
     *
     * <p>If a <code>List</code> was created using 
     * {@link java.util.Collections#unmodifiableList}, this method must
     * return <code>true</code>. Unfortunately, there is no Collections API
     * method to detect this. However, an implementation can create a
     * prototype unmodifiable <code>List</code> and query its runtime type
     * to see if it matches the runtime type of the base object as a 
     * workaround.</p>
     *
     * @param context The context of this evaluation.
     * @param base The list to analyze. Only bases of type <code>List</code>
     *     are handled by this resolver.
     * @param property The index of the element in the list to return the 
     *     acceptable type for. Will be coerced into an integer, but 
     *     otherwise ignored by this resolver.
     * @return If the <code>propertyResolved</code> property of 
     *     <code>ELContext</code> was set to <code>true</code>, then
     *     <code>true</code> if calling the <code>setValue</code> method
     *     will always fail or <code>false</code> if it is possible that
     *     such a call may succeed; otherwise undefined.
     * @throws PropertyNotFoundException if the given index is out of 
     *     bounds for this list.
     * @throws NullPointerException if context is <code>null</code>
     * @throws ELException if an exception was thrown while performing
     *     the property or variable resolution. The thrown exception
     *     must be included as the cause property of this exception, if
     *     available.
     */
    public boolean isReadOnly(ELContext context,
                              Object base,
                              Object property) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (base != null && base instanceof Set) {
            context.setPropertyResolved(true);
            Set set = (Set) base;
            int index = toInteger(property);
            if (index < 0 || index >= set.size()) {
                throw new PropertyNotFoundException();
            } 
            return set.getClass() == theUnmodifiableSetClass || isReadOnly;
        }
        return false;
    }

    /**
     * Always returns <code>null</code>, since there is no reason to 
     * iterate through set set of all integers.
     *
     * <p>The {@link #getCommonPropertyType} method returns sufficient
     * information about what properties this resolver accepts.</p>
     *
     * @param context The context of this evaluation.
     * @param base The list. Only bases of type <code>List</code> are 
     *     handled by this resolver.
     * @return <code>null</code>.
     */
    public Iterator<FeatureDescriptor> getFeatureDescriptors(
                                          ELContext context,
                                          Object base) {
        return null;
    }


    public Class<?> getCommonPropertyType(ELContext context,
                                               Object base) {
        if (base != null && base instanceof Set) {
            return Integer.class;
        }
        return null;
    }
    
    private int toInteger(Object p) {
        if (p instanceof Integer) {
            return ((Integer) p).intValue();
        }
        if (p instanceof Character) {
            return ((Character) p).charValue();
        }
        if (p instanceof Boolean) {
            return ((Boolean) p).booleanValue()? 1: 0;
        }
        if (p instanceof Number) {
            return ((Number) p).intValue();
        }
        if (p instanceof String) {
            return Integer.parseInt((String) p);
        }
        throw new IllegalArgumentException();
    }

    private boolean isReadOnly;

}
