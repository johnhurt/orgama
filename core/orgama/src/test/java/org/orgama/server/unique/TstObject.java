package org.orgama.server.unique;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.orgama.server.annotation.Unique;
import org.orgama.shared.unique.HasIdAndUniqueFields;

/**
 * Object used for testing the uniqueness lock
 *
 * @author kguthrie
 */
@Entity
public class TstObject extends HasIdAndUniqueFields {

	@Id
	Long id;
	
	@Unique
	@Index
	String uniqueField;
	
	@Index
	@Unique
	protected Integer anotherUniqueField;
	
	@Index
	@Unique
	protected Integer yetAnotherUniqueField;
	
	protected int anotherField;

	public TstObject() {
	}

	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the uniqueField
	 */
	public String getUniqueField() {
		return uniqueField;
	}

	/**
	 * @param uniqueField the uniqueField to set
	 */
	public void setUniqueField(String uniqueField) {
		this.uniqueField = uniqueField;
	}

	/**
	 * @return the anotherField
	 */
	public int getAnotherField() {
		return anotherField;
	}

	/**
	 * @param anotherField the anotherField to set
	 */
	public void setAnotherField(int anotherField) {
		this.anotherField = anotherField;
	}

	/**
	 * @return the anotherUniqueField
	 */
	public int getAnotherUniqueField() {
		return anotherUniqueField;
	}

	/**
	 * @param anotherUniqueField the anotherUniqueField to set
	 */
	public void setAnotherUniqueField(int anotherUniqueField) {
		this.anotherUniqueField = anotherUniqueField;
	}

	/**
	 * @return the yetAnotherUniqueField
	 */
	public int getYetAnotherUniqueField() {
		return yetAnotherUniqueField;
	}

	/**
	 * @param yetAnotherUniqueField the yetAnotherUniqueField to set
	 */
	public void setYetAnotherUniqueField(int yetAnotherUniqueField) {
		this.yetAnotherUniqueField = yetAnotherUniqueField;
	}

	@EntitySubclass(index = true)
	public static class TstObject0 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject1 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject2 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject3 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject4 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject5 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject6 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject7 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject8 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject9 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject10 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject11 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject12 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject13 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject14 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject15 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject16 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject17 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject18 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject19 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject20 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject21 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject22 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject23 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject24 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject25 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject26 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject27 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject28 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject29 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject30 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject31 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject32 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject33 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject34 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject35 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject36 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject37 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject38 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject39 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject40 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject41 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject42 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject43 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject44 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject45 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject46 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject47 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject48 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject49 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject50 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject51 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject52 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject53 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject54 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject55 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject56 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject57 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject58 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject59 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject60 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject61 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject62 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject63 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject64 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject65 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject66 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject67 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject68 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject69 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject70 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject71 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject72 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject73 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject74 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject75 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject76 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject77 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject78 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject79 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject80 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject81 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject82 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject83 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject84 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject85 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject86 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject87 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject88 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject89 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject90 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject91 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject92 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject93 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject94 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject95 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject96 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject97 extends TstObject {
	}

	@EntitySubclass(index = true)
	public static class TstObject98 extends TstObject {
	}
	
	@EntitySubclass(index = true)
	public static class TstObject99 extends TstObject {
	}
}
