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

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.CompanyModel;
import com.liferay.portal.model.GroupModel;
import com.liferay.portal.model.LayoutModel;
import com.liferay.portal.model.LayoutSetModel;
import com.liferay.portal.model.LayoutSetPrototype;
import com.liferay.portal.model.LayoutSetPrototypeModel;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.model.RoleModel;
import com.liferay.portal.model.User;
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
import com.liferay.so.util.PortletKeys;
import com.liferay.so.util.RoleConstants;
import com.liferay.so.util.SocialOfficeConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Matthew Kong
 * @author Sherry Yang
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
		initReleases();
	}

	public int getBuildNumber(String key) {
		return _releaseModels.get(key);
	}

	public String getColorSchemeId() {
		return _COLOR_SCHEME_ID;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public String getDate() {
		return getDateString(new Date());
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

	public List<ExpandoValueModel> getExpandoValueModels() {
		return _expandoValueModels;
	}

	public long getGroupExpandoColumnId() {
		return _groupExpandoColumnId;
	}

	public long getGroupExpandoTableId() {
		return _groupExpandoTableId;
	}

	public long getGroupId(long userId) {
		return _userGroupIds.get(userId);
	}

	public String getGroupTypeSettings() {
		return _groupTypeSettings;
	}

	public List<LayoutModel> getLayoutModels() {
		_siteLayoutModels.addAll(_userSourcePrototypeLayoutModels);
		return _siteLayoutModels;
	}

	public List<LayoutSetModel> getLayoutSetModels() {
		return _layoutSetModels;
	}

	public List<GroupModel> getLayoutSetPrototypeGroupModels() {
		return _layoutSetPrototypeGroupModels;
	}

	public List<LayoutSetPrototypeModel> getLayoutSetPrototypeModels() {
		return _layoutSetPrototypeModels;
	}

	public LayoutSetPrototypeModel getLayoutSetPrototypeSiteModel() {
		return _layoutSetPrototypeSiteModel;
	}

	public LayoutSetPrototypeModel getLayoutSetPrototypeUserPrivateModel() {
		return _layoutSetPrototypeUserPrivateModel;
	}

	public LayoutSetPrototypeModel getLayoutSetPrototypeUserPublicModel() {
		return _layoutSetPrototypeUserPublicModel;
	}

	public Set<String> getServletContextNames() {
		return _releaseModels.keySet();
	}

	public List<LayoutModel> getSiteLayoutModels() {
		return _siteLayoutModels;
	}

	public String getSOTypeSettings() {
		return "last-merge-time="+ System.currentTimeMillis();
	}

	public RoleModel getSOUserRoleModel() {
		return _soUserRoleModel;
	}

	public String getThemeId() {
		return _THEME_ID;
	}

	public long getUserClassNameId() {
		return _userClassNameId;
	}

	public Set<Long> getUserIds() {
		return _userGroupIds.keySet();
	}

	public List<LayoutModel> getUserSourcePrototypeLayoutModels() {
		return _userSourcePrototypeLayoutModels;
	}

	public String getUUID() {
		return SequentialUUID.generate();
	}

	public void initExpandos() {
		_groupExpandoColumnId = getCounterNext();
		_groupExpandoTableId = getCounterNext();

		newExpandoColumnModel(
			_groupExpandoColumnId, _groupExpandoTableId, "socialOfficeEnabled",
			ExpandoColumnConstants.BOOLEAN, StringPool.BLANK, StringPool.BLANK);

		newExpandoTableModel(
			_groupExpandoTableId, getGroupClassNameId(),
			ExpandoTableConstants.DEFAULT_TABLE_NAME);

		_layoutSetPrototypeExpandoColumnId = getCounterNext();
		_layoutSetPrototypeExpandoTableId = getCounterNext();

		newExpandoColumnModel(
			_layoutSetPrototypeExpandoColumnId,
			_layoutSetPrototypeExpandoTableId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY,
			ExpandoColumnConstants.STRING, StringPool.BLANK, StringPool.BLANK);

		newExpandoTableModel(
			_layoutSetPrototypeExpandoTableId, _layoutSetPrototypeClassNameId,
			ExpandoTableConstants.DEFAULT_TABLE_NAME);
	}

	public void initLayoutSetPrototype() throws Exception {
		setupLayoutSetPrototypeSite();
		setupLayoutSetPrototypeUserPrivate();
		setupLayoutSetPrototypeUserPublic();
	}

	public void initReleases() {
		_releaseModels.put("contacts-portlet", 200);
		_releaseModels.put("marketplace-portlet", 100);
		_releaseModels.put("private-messaging-portlet", 101);
		_releaseModels.put("so-activities-hook", 101);
		_releaseModels.put("so-hook", 300);
	}

	public void initSOUserRoleModel() {
		_soUserRoleModel = newRoleModel(
			RoleConstants.SOCIAL_OFFICE_USER, RoleConstants.TYPE_REGULAR);
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

	public ExpandoValueModel newExpandoValueModel(
		long valueId, long tableId, long columnId, long rowId, long classNameId,
		long classPK, String data) {

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

	@Override
	public GroupModel newGroupModel(UserModel userModel) throws Exception {
		GroupModel groupModel = super.newGroupModel(userModel);

		_userGroupIds.put(userModel.getUserId(), groupModel.getGroupId());

		return groupModel;
	}

	public LayoutModel newUserLayoutModel(
		long groupId, LayoutModel sourcePrototypeLayout) {

		LayoutModel layoutModel = (LayoutModel)sourcePrototypeLayout.clone();
		layoutModel.setGroupId(groupId);
		layoutModel.setPlid(getCounterNext());
		layoutModel.setLayoutId(getCounterNext());
		layoutModel.setTypeSettings(
			getSOTypeSettings() + StringPool.SPACE +
			sourcePrototypeLayout.getTypeSettings());
		layoutModel.setSourcePrototypeLayoutUuid(
			sourcePrototypeLayout.getUuid());

		return layoutModel;
	}

	protected ExpandoColumnModel newExpandoColumnModel(
		long columnId, long tableId, String name, int type, String defaultData,
		String typeSettings) {

		ExpandoColumnModel expandoColumnModel = new ExpandoColumnImpl();

		expandoColumnModel.setColumnId(columnId);
		expandoColumnModel.setCompanyId(_companyId);
		expandoColumnModel.setTableId(tableId);
		expandoColumnModel.setName(name);
		expandoColumnModel.setType(type);
		expandoColumnModel.setDefaultData(defaultData);
		expandoColumnModel.setTypeSettings(typeSettings);

		_expandoColumnModels.add(expandoColumnModel);

		return expandoColumnModel;
	}

	protected ExpandoTableModel newExpandoTableModel(
		long tableId, long classNameId, String name) {

		ExpandoTableModel expandoTableModel = new ExpandoTableImpl();

		expandoTableModel.setTableId(tableId);
		expandoTableModel.setCompanyId(_companyId);
		expandoTableModel.setClassNameId(classNameId);
		expandoTableModel.setName(name);

		_expandoTableModels.add(expandoTableModel);

		return expandoTableModel;
	}

	protected LayoutModel newLayoutModel(
		long groupId, String name, boolean privateLayout, String templateId,
		String column1, String column2) {

		LayoutModel layoutModel = newLayoutModel(
			groupId, name, StringPool.BLANK, StringPool.BLANK);

		layoutModel.setPrivateLayout(privateLayout);

		String friendlyURL = StringUtil.lowerCase(
			StringUtil.replace(name, StringPool.SPACE, StringPool.DASH));

		layoutModel.setFriendlyURL("/so/" + friendlyURL);

		UnicodeProperties typeSettingsProperties = new UnicodeProperties(true);

		typeSettingsProperties.setProperty(
			LayoutTypePortletConstants.LAYOUT_TEMPLATE_ID, templateId);
		typeSettingsProperties.setProperty("column-1", column1);

		if (Validator.isNotNull(column2)) {
			typeSettingsProperties.setProperty("column-2", column2);
		}

		String typeSettings = StringUtil.replace(
			typeSettingsProperties.toString(), "\n", "\\n");

		layoutModel.setTypeSettings(typeSettings);

		return layoutModel;
	}

	protected LayoutSetPrototypeModel newLayoutSetPrototypeModel(
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

		_layoutSetPrototypeModels.add(layoutSetPrototypeModel);

		return layoutSetPrototypeModel;
	}

	protected LayoutSetModel newSOLayoutSetModel(
		long groupId, boolean privateLayout, int pageCount) {

		LayoutSetModel layoutSetModel = newLayoutSetModel(
			groupId, privateLayout, pageCount);

		layoutSetModel.setThemeId(_THEME_ID);
		layoutSetModel.setColorSchemeId(_COLOR_SCHEME_ID);

		_layoutSetModels.add(layoutSetModel);

		return layoutSetModel;
	}

	protected GroupModel newTemplateGroupModel(
			long groupId, long classNameId, long classPK, String name,
			boolean site)
		throws Exception {

		GroupModel groupModel = super.newGroupModel(
			groupId, classNameId, classPK, name, site);

		groupModel.setTypeSettings(_groupTypeSettings);
		groupModel.setFriendlyURL("/template-" + groupModel.getGroupId());

		return groupModel;
	}

	protected void setupLayoutSetPrototypeSite() throws Exception {
		_siteLayoutSetPrototypeId = getCounterNext();

		_layoutSetPrototypeSiteModel = newLayoutSetPrototypeModel(
			_siteLayoutSetPrototypeId, _defaultUserId,
			"Default Social Office Site", StringPool.BLANK, true);

		long expandoRowId = getCounterNext();

		ExpandoValueModel expandoValueModel = newExpandoValueModel(
			getCounterNext(), _layoutSetPrototypeExpandoTableId,
			_layoutSetPrototypeExpandoColumnId, expandoRowId,
			_layoutSetPrototypeClassNameId, _siteLayoutSetPrototypeId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_SITE);

		_expandoValueModels.add(expandoValueModel);

		ExpandoRowModel expandoRowModel = newExpandoRowModel(
			expandoRowId, _layoutSetPrototypeExpandoTableId,
			_siteLayoutSetPrototypeId);

		_expandoRowModels.add(expandoRowModel);

		_siteLayoutSetPrototypeGroupId = getCounterNext();

		_layoutSetPrototypeGroupModels.add(
			newTemplateGroupModel(
				_siteLayoutSetPrototypeGroupId, _layoutSetPrototypeClassNameId,
				_siteLayoutSetPrototypeId,
				String.valueOf(_siteLayoutSetPrototypeId), false));

		newSOLayoutSetModel(_siteLayoutSetPrototypeGroupId, true, 7);
		newSOLayoutSetModel(_siteLayoutSetPrototypeGroupId, false, 0);

		// Home

		String column1 =
			PortletKeys.SO_ANNOUNCEMENTS + StringPool.COMMA +
				PortletKeys.SO_ACTIVITIES;

		StringBundler sb = new StringBundler(8);

		sb.append("1_WAR_wysiwygportlet_INSTANCE_abcd,");
		sb.append(PortletKeys.BOOKMARKS);
		sb.append(StringPool.COMMA);
		sb.append(PortletKeys.RSS);
		sb.append("_INSTANCE_abcd,");
		sb.append(PortletKeys.RECENT_DOCUMENTS);
		sb.append(StringPool.COMMA);
		sb.append("1_WAR_eventsdisplayportlet");

		//Home

		LayoutModel homeLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Home", true, "2_columns_iii",
			column1, sb.toString());

		_siteLayoutModels.add(homeLayoutModel);

		// Calendar

		LayoutModel calendarLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Calendar", true, "1_column",
			"1_WAR_calendarportlet", null);

		_siteLayoutModels.add(calendarLayoutModel);

		// Documents

		LayoutModel documentsLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Documents", true, "1_column",
			PortletKeys.DOCUMENT_LIBRARY, null);

		_siteLayoutModels.add(documentsLayoutModel);

		// Forums

		column1 =
			PortletKeys.BREADCRUMB + "_INSTANCE_abcd," +
				PortletKeys.MESSAGE_BOARDS;

		LayoutModel forumsLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Forums", true, "1_column", column1,
			null);

		_siteLayoutModels.add(forumsLayoutModel);

		// Blog

		LayoutModel blogLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Blogs", true, "2_columns_iii",
			PortletKeys.BLOGS, PortletKeys.BLOGS_AGGREGATOR);

		_siteLayoutModels.add(blogLayoutModel);

		// Wiki

		LayoutModel wikiLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Wiki", true, "1_column",
			PortletKeys.WIKI, null);

		_siteLayoutModels.add(wikiLayoutModel);

		// Members

		LayoutModel memberLayoutModel = newLayoutModel(
			_siteLayoutSetPrototypeGroupId, "Members", true, "1_column",
			PortletKeys.SO_INVITE_MEMBERS + ",4_WAR_contactsportlet", null);

		_siteLayoutModels.add(memberLayoutModel);
	}

	protected void setupLayoutSetPrototypeUserPrivate() throws Exception {
		_userPrivateLayoutSetPrototypeId = getCounterNext();

		_layoutSetPrototypeUserPrivateModel = newLayoutSetPrototypeModel(
			_userPrivateLayoutSetPrototypeId, _defaultUserId,
			"Social Office User Private Home", StringPool.BLANK, true);

		long expandoRowId = getCounterNext();

		ExpandoValueModel expandoValueModel = newExpandoValueModel(
			getCounterNext(), _layoutSetPrototypeExpandoTableId,
			_layoutSetPrototypeExpandoColumnId, expandoRowId,
			_layoutSetPrototypeClassNameId, _userPrivateLayoutSetPrototypeId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_USER_PRIVATE);

		_expandoValueModels.add(expandoValueModel);

		ExpandoRowModel expandoRowModel = newExpandoRowModel(
			expandoRowId, _layoutSetPrototypeExpandoTableId,
			_userPrivateLayoutSetPrototypeId);

		_expandoRowModels.add(expandoRowModel);

		_userPrivateLayoutSetPrototypeGroupId = getCounterNext();

		_layoutSetPrototypeGroupModels.add(
			newTemplateGroupModel(
				_userPrivateLayoutSetPrototypeGroupId,
				_layoutSetPrototypeClassNameId,
				_userPrivateLayoutSetPrototypeId,
				String.valueOf(_userPrivateLayoutSetPrototypeId), false));

		newSOLayoutSetModel(_userPrivateLayoutSetPrototypeGroupId, true, 6);
		newSOLayoutSetModel(_userPrivateLayoutSetPrototypeGroupId, false, 0);

		// Dashboard

		LayoutModel dashboardLayoutModel = newLayoutModel(
			_userPrivateLayoutSetPrototypeGroupId, "Dashboard", true,
			"2_columns_iii",
			"2_WAR_microblogsportlet,1_WAR_soannouncementsportlet," +
				"1_WAR_soportlet",
			"2_WAR_tasksportlet,1_WAR_eventsdisplayportlet");

		_userSourcePrototypeLayoutModels.add(dashboardLayoutModel);

		// Contacts Center

		LayoutModel contactsLayoutModel = newLayoutModel(
			_userPrivateLayoutSetPrototypeGroupId, "Contacts Center", true,
			"1_column", "1_WAR_contactsportlet", null);

		_userSourcePrototypeLayoutModels.add(contactsLayoutModel);

		// Microblogs

		LayoutModel microblogsLayoutModel = newLayoutModel(
			_userPrivateLayoutSetPrototypeGroupId, "Microblogs", true,
			"1_column", "1_WAR_microblogsportlet", null);

		_userSourcePrototypeLayoutModels.add(microblogsLayoutModel);

		// Messages

		LayoutModel messagesLayoutModel = newLayoutModel(
			_userPrivateLayoutSetPrototypeGroupId, "Messages", true, "1_column",
			"1_WAR_privatemessagingportlet", null);

		_userSourcePrototypeLayoutModels.add(messagesLayoutModel);

		// Documents

		LayoutModel documentsLayoutModel = newLayoutModel(
			_userPrivateLayoutSetPrototypeGroupId, "Documents", true,
			"1_column", PortletKeys.DOCUMENT_LIBRARY, null);

		_userSourcePrototypeLayoutModels.add(documentsLayoutModel);

		// Tasks

		LayoutModel tasksLayoutModel = newLayoutModel(
			_userPrivateLayoutSetPrototypeGroupId, "Tasks", true, "1_column",
			"1_WAR_tasksportlet", null);

		_userSourcePrototypeLayoutModels.add(tasksLayoutModel);
	}

	protected void setupLayoutSetPrototypeUserPublic() throws Exception {
		_userPublicLayoutSetPrototypeId = getCounterNext();

		_layoutSetPrototypeUserPublicModel = newLayoutSetPrototypeModel(
			_userPublicLayoutSetPrototypeId, _defaultUserId,
			"Social Office User Public Home", StringPool.BLANK, true);

		long expandoRowId = getCounterNext();

		ExpandoValueModel expandoValueModel = newExpandoValueModel(
			getCounterNext(), _layoutSetPrototypeExpandoTableId,
			_layoutSetPrototypeExpandoColumnId, expandoRowId,
			_layoutSetPrototypeClassNameId, _userPublicLayoutSetPrototypeId,
			SocialOfficeConstants.LAYOUT_SET_PROTOTYPE_KEY_USER_PRIVATE);

		_expandoValueModels.add(expandoValueModel);

		ExpandoRowModel expandoRowModel = newExpandoRowModel(
			expandoRowId, _layoutSetPrototypeExpandoTableId,
			_userPublicLayoutSetPrototypeId);

		_expandoRowModels.add(expandoRowModel);

		_userPublicLayoutSetPrototypeGroupId = getCounterNext();

		_layoutSetPrototypeGroupModels.add(
			newTemplateGroupModel(
				_userPublicLayoutSetPrototypeGroupId,
				_layoutSetPrototypeClassNameId, _userPublicLayoutSetPrototypeId,
				String.valueOf(_userPublicLayoutSetPrototypeId), false));

		newSOLayoutSetModel(_userPublicLayoutSetPrototypeGroupId, true, 0);
		newSOLayoutSetModel(_userPublicLayoutSetPrototypeGroupId, false, 3);

		// Profile

		LayoutModel profileLayoutModel = newLayoutModel(
			_userPublicLayoutSetPrototypeGroupId, "Profile", false,
			"1_2_columns_ii", "2_WAR_contactsportlet_INSTANCE_abcd",
			"2_WAR_microblogsportlet,2_WAR_contactsportlet_INSTANCE_efgh," +
				PortletKeys.SO_ACTIVITIES);

		_userSourcePrototypeLayoutModels.add(profileLayoutModel);

		// Contacts

		LayoutModel contactsLayoutModel = newLayoutModel(
			_userPublicLayoutSetPrototypeGroupId, "Contacts", false, "1_column",
			"2_WAR_contactsportlet_INSTANCE_abcd,1_WAR_contactsportlet", null);

		_userSourcePrototypeLayoutModels.add(contactsLayoutModel);

		// Microblogs

		LayoutModel MicroblogsModel = newLayoutModel(
			_userPublicLayoutSetPrototypeGroupId, "Microblogs", false,
			"1_column",
			"2_WAR_contactsportlet_INSTANCE_abcd,1_WAR_microblogsportlet",
			null);

		_userSourcePrototypeLayoutModels.add(MicroblogsModel);
	}

	private static final String _COLOR_SCHEME_ID = "01";

	private static final String _THEME_ID = "so_WAR_sotheme";

	private long _companyId;
	private long _defaultUserId;
	private List<ExpandoColumnModel> _expandoColumnModels =
		new ArrayList<ExpandoColumnModel>();
	private List<ExpandoRowModel> _expandoRowModels =
		new ArrayList<ExpandoRowModel>();
	private List<ExpandoTableModel> _expandoTableModels =
		new ArrayList<ExpandoTableModel>();
	private List<ExpandoValueModel> _expandoValueModels =
		new ArrayList<ExpandoValueModel>();
	private long _groupExpandoColumnId;
	private long _groupExpandoTableId;
	private String _groupTypeSettings = "customJspServletContextName=so-hook";
	private List<LayoutSetModel> _layoutSetModels =
		new ArrayList<LayoutSetModel>();
	private long _layoutSetPrototypeClassNameId = getClassNameId(
		LayoutSetPrototype.class.getName());
	private long _layoutSetPrototypeExpandoColumnId;
	private long _layoutSetPrototypeExpandoTableId;
	private List<GroupModel> _layoutSetPrototypeGroupModels =
		new ArrayList<GroupModel>();
	private List<LayoutSetPrototypeModel> _layoutSetPrototypeModels =
		new ArrayList<LayoutSetPrototypeModel>();
	private LayoutSetPrototypeModel _layoutSetPrototypeSiteModel;
	private LayoutSetPrototypeModel _layoutSetPrototypeUserPrivateModel;
	private LayoutSetPrototypeModel _layoutSetPrototypeUserPublicModel;
	private Map<String, Integer> _releaseModels =
		new HashMap<String, Integer>();
	private List<LayoutModel> _siteLayoutModels = new ArrayList<LayoutModel>();
	private long _siteLayoutSetPrototypeGroupId;
	private long _siteLayoutSetPrototypeId;
	private RoleModel _soUserRoleModel;
	private long _userClassNameId = getClassNameId(User.class.getName());
	private Map<Long, Long> _userGroupIds = new HashMap<Long, Long>();
	private long _userPrivateLayoutSetPrototypeGroupId;
	private long _userPrivateLayoutSetPrototypeId;
	private long _userPublicLayoutSetPrototypeGroupId;
	private long _userPublicLayoutSetPrototypeId;
	private List<LayoutModel> _userSourcePrototypeLayoutModels =
		new ArrayList<LayoutModel>();

}