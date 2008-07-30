/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.twitter.service;

/**
 * <a href="FeedLocalServiceUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class FeedLocalServiceUtil {
	public static com.liferay.twitter.model.Feed addFeed(
		com.liferay.twitter.model.Feed feed)
		throws com.liferay.portal.SystemException {
		return _service.addFeed(feed);
	}

	public static void deleteFeed(long feedId)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		_service.deleteFeed(feedId);
	}

	public static void deleteFeed(com.liferay.twitter.model.Feed feed)
		throws com.liferay.portal.SystemException {
		_service.deleteFeed(feed);
	}

	public static java.util.List<Object> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.SystemException {
		return _service.dynamicQuery(dynamicQuery);
	}

	public static java.util.List<Object> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.SystemException {
		return _service.dynamicQuery(dynamicQuery, start, end);
	}

	public static com.liferay.twitter.model.Feed getFeed(long feedId)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		return _service.getFeed(feedId);
	}

	public static java.util.List<com.liferay.twitter.model.Feed> getFeeds(
		int start, int end) throws com.liferay.portal.SystemException {
		return _service.getFeeds(start, end);
	}

	public static int getFeedsCount() throws com.liferay.portal.SystemException {
		return _service.getFeedsCount();
	}

	public static com.liferay.twitter.model.Feed updateFeed(
		com.liferay.twitter.model.Feed feed)
		throws com.liferay.portal.SystemException {
		return _service.updateFeed(feed);
	}

	public static void updateFeed(long userId)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		_service.updateFeed(userId);
	}

	public static void updateFeeds()
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		_service.updateFeeds();
	}

	public static void updateFeeds(long companyId)
		throws com.liferay.portal.PortalException,
			com.liferay.portal.SystemException {
		_service.updateFeeds(companyId);
	}

	public static FeedLocalService getService() {
		return _service;
	}

	public void setService(FeedLocalService service) {
		_service = service;
	}

	private static FeedLocalService _service;
}