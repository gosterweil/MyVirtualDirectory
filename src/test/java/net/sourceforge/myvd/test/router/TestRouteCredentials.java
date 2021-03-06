/*
 * Copyright 2008 Marc Boorshtein 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package net.sourceforge.myvd.test.router;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPSearchResults;

import net.sourceforge.myvd.test.util.OpenLDAPUtils;
import net.sourceforge.myvd.test.util.StartMyVD;
import net.sourceforge.myvd.test.util.StartOpenLDAP;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestRouteCredentials  {

	private static StartOpenLDAP internalServer;
	private static StartMyVD server;
	
	@BeforeClass
	public static void setUp() throws Exception {
		OpenLDAPUtils.killAllOpenLDAPS();
		
		internalServer = new StartOpenLDAP();
		internalServer.startServer(System.getenv("PROJ_DIR") + "/test/Base",10983,"cn=admin,dc=domain,dc=com","manager");
		
		server = new StartMyVD();
		server.startServer(System.getenv("PROJ_DIR") + "/test/TestServer/myvd-router.props",50983);
	}
	
	@Test
	public void testStartup () throws Exception {
	
	}
	
	@Test
	public void testAdminBind() throws Exception {
		LDAPConnection con = new LDAPConnection();
		con.connect("127.0.0.1", 50983);
		
		con.bind(3,"cn=admin,dc=domain,dc=com", "manager".getBytes());
		con.disconnect();
		
	}
	
	@Test
	public void testAdminBindSearch() throws Exception {
		LDAPConnection con = new LDAPConnection();
		con.connect("127.0.0.1", 50983);
		
		//con.bind(3,"cn=admin,dc=domain,dc=com", "manager".getBytes());
		
		LDAPSearchResults res = con.search("dc=domain,dc=com", 2,"(objectClass=*)" , new String[] {"1.1"}, false);
		
		while (res.hasMore()) {
			res.next();
		}
		
		con.disconnect();
		
	}
	
	@Test
	public void testAdminBindSearchSchema() throws Exception {
		LDAPConnection con = new LDAPConnection();
		con.connect("127.0.0.1", 50983);
		
		con.bind(3,"cn=admin,dc=domain,dc=com", "manager".getBytes());
		
		LDAPSearchResults res = con.search("cn=schema", 0,"(objectClass=*)" , new String[] {"objectClasses"}, false);
		
		while (res.hasMore()) {
			res.next();
		}
		
		con.disconnect();
		
	}

	@AfterClass
	public static void tearDown() throws Exception {
		
		server.stopServer();
		internalServer.stopServer();
	}

}
