package org.orgama.server.unique;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.orgama.server.annotation.Unique;
import org.orgama.shared.unique.HasIdAndUniqueFields;

/**
 * Test entity that cannot be registered because one of its unique fields is not
 * indexed
 * @author kguthrie
 */
@Entity
public class TstFailingObject extends HasIdAndUniqueFields<Long> {

	@Id
	Long id;
	
	@Unique
	@Index
	String str;
	
	@Unique
	double thisFieldFails;

	@Override
	public Long getId() {
		return id;
	}
			
	
}
