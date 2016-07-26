/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dachen.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class DoctorDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(10, "com.dachen.dgroupdoctor.db.circle");

        //添加Note对象的Schema，会生成针对Note的数据库相关代码
        addGroupList(schema);

        addDepartmentList(schema);
        
        addCircleDoctor(schema);
        
        addCircleFriend(schema);

		addEnterpriseFriend(schema);//外部好友（医药代表好友）

		addUserTag(schema);//用户标签
        
        new DaoGenerator().generateAll(schema, "C:/Users/TianWei/Desktop/dataBase");
    }

    /**
     * @param schema
     */
    private static void addGroupList(Schema schema) {
    	Entity note = schema.addEntity("CircleGroup");
    	note.addStringProperty("id").unique().notNull().primaryKey();
    	note.addStringProperty("name");
    	note.addStringProperty("introduction");
    	note.addStringProperty("certStatus");
    	note.addStringProperty("companyId");
    	note.addStringProperty("creator");
    }
    
    /**
     * @param schema
     */
    private static void addDepartmentList(Schema schema) {
    	Entity note = schema.addEntity("CircleDepartment");
    	note.addStringProperty("id").unique().notNull().primaryKey();
    	note.addStringProperty("name");
    	note.addStringProperty("groupId");
    	note.addStringProperty("parentId");
    	note.addStringProperty("description");
    	note.addStringProperty("creator");
    }
    
    
    /**
     * @param schema
     */
    private static void addCircleFriend(Schema schema) {
    	Entity note = schema.addEntity("CircleFriender");
    	note.addStringProperty("userId").unique().notNull().primaryKey();
    	note.addStringProperty("area");
    	note.addStringProperty("departments");
    	note.addStringProperty("doctorNum");
    	note.addStringProperty("creator");
    	note.addStringProperty("headPicFileName");
    	note.addStringProperty("hospital");
    	note.addStringProperty("name");
    	note.addStringProperty("sex");
    	note.addStringProperty("status");
    	note.addStringProperty("telephone");
    	note.addStringProperty("title");
    	note.addStringProperty("letter");
    }
	/**
	 * @param schema
	 */
	private static void addEnterpriseFriend(Schema schema) {
		Entity note = schema.addEntity("CircleEnterpriseFriend");
		note.addStringProperty("userId").unique().notNull().primaryKey();
		note.addStringProperty("id");
		note.addStringProperty("name");
		note.addStringProperty("companyName");
		note.addStringProperty("department");
		note.addStringProperty("position");
		note.addStringProperty("remarks");
		note.addStringProperty("status");
		note.addStringProperty("telephone");
		note.addStringProperty("letter");
		note.addStringProperty("headPicFileName");
	}
    
    /**
     * @param schema
     */
    private static void addCircleDoctor(Schema schema) {
    	Entity note = schema.addEntity("CircleDoctor");
    	note.addStringProperty("id").unique().notNull().primaryKey();
    	note.addStringProperty("contactWay");
    	note.addStringProperty("departmentId");
    	note.addStringProperty("groupId");
    	note.addStringProperty("main");
		note.addStringProperty("departmentName");
    	note.addStringProperty("referenceId");
    	note.addStringProperty("status");
    	note.addStringProperty("departments");
    	note.addStringProperty("doctorId");
    	note.addStringProperty("doctorNum");
    	note.addStringProperty("headPicFilePath");
    	note.addStringProperty("hospital");
    	note.addStringProperty("introduction");
    	note.addStringProperty("name");
    	note.addStringProperty("position");
    	note.addStringProperty("skill");
    	note.addStringProperty("telephone");
    	note.addStringProperty("userType");
    }

	/**
	 * @param schema
	 */
	private static void addUserTag(Schema schema) {
		Entity note = schema.addEntity("UserTags");
		note.addLongProperty("_id").unique().primaryKey().autoincrement();
		note.addStringProperty("tagName");
		note.addStringProperty("userId");
		note.addStringProperty("userType");
		note.addStringProperty("tagId");
		note.addStringProperty("sys");
	}

    @SuppressWarnings("unused")
	private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}
