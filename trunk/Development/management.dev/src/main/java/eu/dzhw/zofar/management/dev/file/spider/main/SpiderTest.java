package eu.dzhw.zofar.management.dev.file.spider.main;

import eu.dzhw.zofar.management.dev.file.spider.SpiderClient;
import eu.dzhw.zofar.management.dev.file.spider.components.impl.AddTextHandler;

public class SpiderTest {

	public static void main(String[] args) {
		final SpiderClient spiderClient = SpiderClient.getInstance();
		final String path = "xxxx";
		final String header = "/* Zofar Load Simulator\n* Copyright (C) 2014 Deutsches Zentrum für Hochschul- und Wissenschaftsforschung\n* \n* This program is free software: you can redistribute it and/or modify\n* it under the terms of the GNU General Public License as published by\n* the Free Software Foundation, either version 3 of the License, or\n* (at your option) any later version.\n* \n* This program is distributed in the hope that it will be useful,\n* but WITHOUT ANY WARRANTY; without even the implied warranty of\n* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n* GNU General Public License for more details.\n* \n* You should have received a copy of the GNU General Public License\n* along with this program. If not, see <http://www.gnu.org/licenses/>.\n*/";
		spiderClient.doSpider(path, true, true, null, new AddTextHandler(header,"java"),"/*START HEADER*/","/*STOP HEADER*/");
	}

}
