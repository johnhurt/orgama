package org.orgama.server.mock;

import com.google.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 *
 * @author kguthrie
 */
public class MockSerializingHttpSession implements HttpSession {

	private Map<String, byte[]> storage;
	
	@Inject
	public MockSerializingHttpSession() {
		storage = new HashMap<String, byte[]>();
	}
	
	@Override
	public long getCreationTime() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getId() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public long getLastAccessedTime() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ServletContext getServletContext() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int getMaxInactiveInterval() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Object getAttribute(String name) {
		return getValue(name);
	}

	@Override
	public Object getValue(String name) {
		byte[] serialized = storage.get(name);
		
		if (serialized == null) {
			return null;
		}
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(serialized);

			ObjectInputStream ois = new ObjectInputStream(bais);
			
			Object result = ois.readObject();
			
			ois.close();
			
			return result;
		}
		catch(Exception ex) {
			return null;
		}
	}

	@Override
	public Enumeration getAttributeNames() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String[] getValueNames() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setAttribute(String name, Object value) {
		putValue(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(value);
			oos.flush();
			
			storage.put(name, baos.toByteArray());
			
			oos.close();
		}
		catch(Exception ex) {
			
		}
	}

	@Override
	public void removeAttribute(String name) {
		storage.remove(name);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void invalidate() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public boolean isNew() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
