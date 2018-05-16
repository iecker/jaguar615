/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.controller.mock;


import java.security.Principal;


/**
 * <p>Mock <strong>Principal</strong> object for low-level unit tests
 * of Struts controller components.  Coarser grained tests should be
 * implemented in terms of the Cactus framework, instead of the mock
 * object classes.</p>
 *
 * <p><strong>WARNING</strong> - Only the minimal set of methods needed to
 * create unit tests is provided, plus additional methods to configure this
 * object as necessary.  Methods for unsupported operations will throw
 * <code>UnsupportedOperationException</code>.</p>
 *
 * <p><strong>WARNING</strong> - Because unit tests operate in a single
 * threaded environment, no synchronization is performed.</p>
 *
 * @since jCompany 2.7.3
 */

public class PrincipalMock implements Principal {


	/**
	 * @since jCompany 2.7.3
	 */
    public PrincipalMock() {
        super();
        this.name = "";
        this.roles = new String[0];
    }


	/**
	 * @since jCompany 2.7.3
	 */
    public PrincipalMock(String name) {
        super();
        this.name = name;
        this.roles = new String[0];
    }


	/**
	 * @since jCompany 2.7.3
	 */
    public PrincipalMock(String name, String roles[]) {
        super();
        this.name = name;
        this.roles = roles;
    }


    protected String name = null;


    protected String roles[] = null;


	/**
	 * @since jCompany 2.7.3
	 */
    public String getName() {
        return (this.name);
    }


	/**
	 * @since jCompany 2.7.3
	 */
    public boolean isUserInRole(String role) {
        for (int i = 0; i < roles.length; i++) {
            if (role.equals(roles[i])) {
                return (true);
            }
        }
        return (false);
    }


	/**
	 * @since jCompany 2.7.3
	 */
    public boolean equals(Object o) {
        if (o == null) {
            return (false);
        }
        if (!(o instanceof Principal)) {
            return (false);
        }
        Principal p = (Principal) o;
        if (name == null) {
            return (p.getName() == null);
        } else {
            return (name.equals(p.getName()));
        }
    }


	/**
	 * @since jCompany 2.7.3
	 */
    public int hashCode() {
        if (name == null) {
            return ("".hashCode());
        } else {
            return (name.hashCode());
        }
    }


}
