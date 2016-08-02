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
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class DoctorCompanyGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "db");
		addCompanyContact(schema);
        new DaoGenerator().generateAll(schema, "C:/Users/TianWei/Desktop/dataBase");
    }

	/**
	 * @param schema
	 */
	private static void addCompanyContact(Schema schema) {
		Entity note = schema.addEntity("UserTags");
		note.addLongProperty("_id").unique().primaryKey().autoincrement();
		note.addStringProperty("tagName");
		note.addStringProperty("userId");
		note.addStringProperty("userType");
		note.addStringProperty("tagId");
		note.addStringProperty("sys");
	}


}
