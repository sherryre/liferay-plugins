/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.so.tools;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.model.GroupModel;
import com.liferay.portal.model.LayoutSetModel;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutSetPrototypeModel;
import com.liferay.portal.model.RoleModel;
import com.liferay.portal.model.UserModel;
import com.liferay.portal.model.impl.LayoutSetPrototypeModelImpl;
import com.liferay.portal.tools.samplesqlbuilder.DataFactory;
import com.liferay.portal.tools.samplesqlbuilder.SequentialUUID;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoColumnModel;
import com.liferay.portlet.expando.model.ExpandoRowModel;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.model.ExpandoTableModel;
import com.liferay.portlet.expando.model.ExpandoValueModel;
import com.liferay.portlet.expando.model.impl.ExpandoColumnImpl;
import com.liferay.portlet.expando.model.impl.ExpandoRowModelImpl;
import com.liferay.portlet.expando.model.impl.ExpandoTableImpl;
import com.liferay.portlet.expando.model.impl.ExpandoValueImpl;
import com.liferay.so.util.RoleConstants;
import com.liferay.so.util.SocialOfficeConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author Matthew Kong
 */
public class SODataFactory extends DataFactory {

	public SODataFactory(Properties properties) throws Exception {
		super(properties);

		CompanyModel companyModel = getCompanyModel();

		_companyId = companyModel.getCompanyId();

		UserModel defaultUserModel = getDefaultUserModel();

		_defaultUserId = defaultUserModel.getUserId();

		initExpandos();
		initSOUserRoleModel();

		initLayoutSetPrototype();
	}

	public void initExpandos() {
		_expandoColumnModels = new ArrayList<ExpandoColumnModel>();
		_expandoTableModels = new ArrayList<ExpandoTableModel>();

		_groupExpandoColumnId = getCounterNext();
		_groupExpandoTableId = getCounterNext();

		_expandoColumnModels.add(
			newExpandoColumnModel(
				_groupExpandoColumnId, _groupExpandoTableId,
				"socialOfficeEnabled", ExpandoColumnConstants.BOOLEAN,
				StringPool.BLANK, StringPool.BLANK));

		_expandoTableModels.add(
			newExpandoTableModel(
				_groupExpandoTableId, getGroupClassNameId(),
				ExpandoTableConstants.DEFAULT_TABLE_NAME));

		_layoutSetPrototypeExpandoColumnId = getCounterNext();
		_layoutSetPrototypeExpandoTableId = getCounterNext();

		_expandoColumnModels.add(
			newExpandoColumnModel(
				_layoutSetPrototypeExpandoColumnId,
				_layoutSetPrototypeExpandoTableId,
				SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY,
				ExpandoColumnConstants.STRING, StringPool.BLANK,
				StringPool.BLANK));

		_expandoTableModels.add(
			newExpandoTableModel(
				_layoutSetPrototypeExpandoTableId,
				getClassNameId(LayoutSetPrototype.class.getName()),
				ExpandoTableConstants.DEFAULT_TABLE_NAME));
	}

	protected ExpandoColumnModel newExpandoColumnModel(
		long columnId, long tableId, String name, int type,
		String defaultData, String typeSettings) {

		ExpandoColumnModel expandoColumnModel = new ExpandoColumnImpl();

		expandoColumnModel.setColumnId(columnId);
		expandoColumnModel.setCompanyId(_companyId);
		expandoColumnModel.setTableId(tableId);
		expandoColumnModel.setName(name);
		expandoColumnModel.setType(type);
		expandoColumnModel.setDefaultData(defaultData);
		expandoColumnModel.setTypeSettings(typeSettings);

		return expandoColumnModel;
	}

	protected ExpandoTableModel newExpandoTableModel(
		long tableId, long classNameId, String name) {

		ExpandoTableModel expandoTableModel = new ExpandoTableImpl();

		expandoTableModel.setTableId(tableId);
		expandoTableModel.setCompanyId(_companyId);
		expandoTableModel.setClassNameId(classNameId);
		expandoTableModel.setName(name);

		return expandoTableModel;
	}

	public void initSOUserRoleModel() {
		_soUserRoleModel = newRoleModel(
			RoleConstants.SOCIAL_OFFICE_USER, RoleConstants.TYPE_REGULAR);
	}

	public RoleModel getSOUserRoleModel() {
		return _soUserRoleModel;
	}

	public List<ExpandoColumnModel> getExpandoColumnModels() {
		return _expandoColumnModels;
	}

	public List<ExpandoRowModel> getExpandoRowModels() {
		return _expandoRowModels;
	}

	public List<ExpandoTableModel> getExpandoTableModels() {
		return _expandoTableModels;
	}

	public List<LayoutSetPrototypeModel> getLayoutSetPrototypeModels() {
		return _layoutSetPrototypeModels;
	}


	public void initLayoutSetPrototype() throws Exception {
		_layoutSetPrototypeModels = new ArrayList<LayoutSetPrototypeModel>();

		_defaultSOSiteLayoutSetPrototypeId = getCounterNext();
		long classNameId = getClassNameId(LayoutSetPrototype.class.getName());

		_layoutSetPrototypeModels.add(
			newLayoutSetPrototypeModel(
				_defaultSOSiteLayoutSetPrototypeId, _defaultUserId,
				"Default Social Office Site", StringPool.BLANK, true));

		_expandoValueModels = new ArrayList<ExpandoValueModel>();

		long rowId = getCounterNext();

		_expandoValueModels.add(
			newExpandoValueModel(
				getCounterNext(), _layoutSetPrototypeExpandoTableId,
				_layoutSetPrototypeExpandoColumnId, rowId,
				classNameId, _defaultSOSiteLayoutSetPrototypeId,
				SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_SITE));

		_expandoRowModels = new ArrayList<ExpandoRowModel>();

		_expandoRowModels.add(
			newExpandoRowModel(
				rowId, _layoutSetPrototypeExpandoTableId,
				_defaultSOSiteLayoutSetPrototypeId));

		//make private
		long _layoutSetPrototypeGroupId = getCounterNext();
		//make private
		GroupModel _defaultSOSiteLayoutSetPrototypeGroupModel = newGroupModel(
			_layoutSetPrototypeGroupId, classNameId,
			_defaultSOSiteLayoutSetPrototypeId,
			String.valueOf(_defaultSOSiteLayoutSetPrototypeId), false);

		long groupId = _defaultSOSiteLayoutSetPrototypeGroupModel.getGroupId();

		_layoutSetModels = new ArrayList<LayoutSetModel>();

		_layoutSetModels.add(newLayoutSetModel(groupId, true, 7));
		_layoutSetModels.add(newLayoutSetModel(groupId, false, 0));


	}

	@Override
	protected LayoutSetModel newLayoutSetModel(
		long groupId, boolean privateLayout, int pageCount) {

		LayoutSetModel layoutSetModel = super.newLayoutSetModel(
			groupId, privateLayout, pageCount);

		layoutSetModel.setThemeId("so_WAR_sotheme");

		return layoutSetModel;
	}

	public ExpandoValueModel newExpandoValueModel(
		long valueId, long tableId, long columnId, long rowId,
		long classNameId, long classPK, String data) {

		ExpandoValueModel expandoValueModel = new ExpandoValueImpl();

		expandoValueModel.setValueId(valueId);
		expandoValueModel.setCompanyId(_companyId);
		expandoValueModel.setTableId(tableId);
		expandoValueModel.setColumnId(columnId);
		expandoValueModel.setRowId(rowId);
		expandoValueModel.setClassNameId(classNameId);
		expandoValueModel.setClassPK(classPK);
		expandoValueModel.setData(data);

		return expandoValueModel;
	}

	public ExpandoRowModel newExpandoRowModel(
		long rowId, long tableId, long classPK) {

		ExpandoRowModel expandoRowModel = new ExpandoRowModelImpl();

		expandoRowModel.setRowId(rowId);
		expandoRowModel.setCompanyId(_companyId);
		expandoRowModel.setModifiedDate(new Date());
		expandoRowModel.setTableId(tableId);
		expandoRowModel.setClassPK(classPK);

		return expandoRowModel;
	}

	public LayoutSetPrototypeModel newLayoutSetPrototypeModel(
		long layoutSetPrototypeId, long userId, String name, String description,
		boolean active) {

		LayoutSetPrototypeModel layoutSetPrototypeModel =
			new LayoutSetPrototypeModelImpl();

		layoutSetPrototypeModel.setUuid(SequentialUUID.generate());
		layoutSetPrototypeModel.setLayoutSetPrototypeId(layoutSetPrototypeId);
		layoutSetPrototypeModel.setCompanyId(_companyId);
		layoutSetPrototypeModel.setUserId(userId);
		layoutSetPrototypeModel.setCreateDate(new Date());
		layoutSetPrototypeModel.setModifiedDate(new Date());

		StringBundler sb = new StringBundler(5);

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root ");
		sb.append("available-locales=\"en_US\" default-locale=\"en_US\">");
		sb.append("<Name language-id=\"en_US\"> ");
		sb.append(name);
		sb.append("</Name></root>");

		layoutSetPrototypeModel.setName(sb.toString());

		sb = new StringBundler(5);

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root ");
		sb.append("available-locales=\"en_US\" default-locale=\"en_US\">");
		sb.append("<Description language-id=\"en_US\"> ");
		sb.append(name);
		sb.append("</Description></root>");

		layoutSetPrototypeModel.setDescription(description);

		UnicodeProperties typeSettingsProperties = new UnicodeProperties(true);

		typeSettingsProperties.setProperty(
			"layoutsUpdateable", StringPool.TRUE);
		typeSettingsProperties.setProperty(
			"customJspServletContextName", "so-hook");

		String typeSettings = StringUtil.replace(
			typeSettingsProperties.toString(), "\n", "\\n");

		layoutSetPrototypeModel.setSettings(typeSettings);

		layoutSetPrototypeModel.setActive(active);

		return layoutSetPrototypeModel;
	}

	private long _companyId;
	private long _defaultSOSiteLayoutSetPrototypeId;
	private long _defaultUserId;
	private List<ExpandoColumnModel> _expandoColumnModels;
	private List<ExpandoRowModel> _expandoRowModels;
	private List<ExpandoTableModel> _expandoTableModels;
	private List<ExpandoValueModel> _expandoValueModels;
	private long _groupExpandoColumnId;
	private long _groupExpandoTableId;
	private List<LayoutSetModel> _layoutSetModels;
	private long _layoutSetPrototypeExpandoColumnId;
	private long _layoutSetPrototypeExpandoTableId;
	private List<LayoutSetPrototypeModel> _layoutSetPrototypeModels;
	private RoleModel _soUserRoleModel;

}