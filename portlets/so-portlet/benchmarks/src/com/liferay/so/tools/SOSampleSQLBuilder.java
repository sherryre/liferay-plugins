/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This file is part of Liferay Social Office. Liferay Social Office is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Liferay Social Office is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Liferay Social Office. If not, see http://www.gnu.org/licenses/agpl-3.0.html.
 */

package com.liferay.so.tools;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.SortedProperties;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.tools.ToolDependencies;
import com.liferay.portal.tools.samplesqlbuilder.DataFactory;
import com.liferay.portal.tools.samplesqlbuilder.SampleSQLBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.util.Properties;

/**
 * @author Tina Tian
 */
public class SOSampleSQLBuilder {

	public static void main(String[] args) {
		ToolDependencies.wireBasic();

		Reader reader = null;

		try {
			Properties properties = new SortedProperties();

			reader = new FileReader(args[0]);

			properties.load(reader);

			DataFactory dataFactory = new SODataFactory(properties);

			new SOSampleSQLBuilder(properties, dataFactory);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	public SOSampleSQLBuilder(Properties properties, DataFactory dataFactory)
		throws Exception {

		new SampleSQLBuilder(properties, dataFactory);

		String portalOutputDir = properties.getProperty(
			"sample.sql.output.dir");

		File output = new File(portalOutputDir, "plugin");

		FileUtil.deltree(output);

		output.mkdirs();

		properties.put("sample.sql.output.dir", output.getPath());
		properties.put("sample.sql.output.csv.file.names", "so");
		properties.put(
			"sample.sql.script", "com/liferay/so/tools/dependencies/so.ftl");

		new SampleSQLBuilder(properties, dataFactory);

		String dbType = properties.getProperty("sample.sql.db.type");
		String sqlDir = properties.getProperty("sql.dir");

		buildPluginSqls(dbType, sqlDir, output);
	}

	protected void buildPluginSqls(String dbType, String sqlDir, File outputDir)
		throws Exception {

		DB db = DBFactoryUtil.getDB(dbType);

		File dir = new File(sqlDir);

		for (File sqlFile : dir.listFiles()) {
			String sqlFileName = sqlFile.getName();

			if (!sqlFileName.endsWith(_SQL_FILE_SUFFIX)) {
				continue;
			}

			StringBundler sb = new StringBundler(4);

			int index = sqlFileName.indexOf(_SQL_FILE_SUFFIX);

			sb.append(sqlFileName.substring(0, index));
			sb.append(StringPool.MINUS);
			sb.append(dbType);
			sb.append(_SQL_FILE_SUFFIX);

			File newSqlFile = new File(outputDir, sb.toString());

			String line = null;
			Writer writer = null;
			UnsyncBufferedReader unsyncBufferedReader = null;

			try {
				writer = new FileWriter(newSqlFile);

				Reader reader = new FileReader(sqlFile);

				unsyncBufferedReader = new UnsyncBufferedReader(reader);

				while ((line = unsyncBufferedReader.readLine()) != null) {
					line = line.trim();

					if (line.length() == 0) {
						continue;
					}

					line = db.buildSQL(line);

					writer.write(line);
				}
			}
			finally {
				if (unsyncBufferedReader != null) {
					unsyncBufferedReader.close();
				}

				if (writer != null) {
					writer.close();
				}
			}
		}
	}

	private static final String _SQL_FILE_SUFFIX = ".sql";

}