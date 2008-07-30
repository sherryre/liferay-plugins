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

package com.liferay.knowledgebase.service;

import com.liferay.util.bean.PortletBeanLocatorUtil;

/**
 * <a href="KBArticleLocalServiceFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 *
 */
public class KBArticleLocalServiceFactory {
	public static KBArticleLocalService getService() {
		return _getFactory()._service;
	}

	public static KBArticleLocalService getImpl() {
		if (_impl == null) {
			_impl = (KBArticleLocalService)PortletBeanLocatorUtil.locate(_IMPL);
		}

		return _impl;
	}

	public static KBArticleLocalService getTxImpl() {
		if (_txImpl == null) {
			_txImpl = (KBArticleLocalService)PortletBeanLocatorUtil.locate(_TX_IMPL);
		}

		return _txImpl;
	}

	public void setService(KBArticleLocalService service) {
		_service = service;
	}

	private static KBArticleLocalServiceFactory _getFactory() {
		if (_factory == null) {
			_factory = (KBArticleLocalServiceFactory)PortletBeanLocatorUtil.locate(_FACTORY);
		}

		return _factory;
	}

	private static final String _FACTORY = KBArticleLocalServiceFactory.class.getName();
	private static final String _IMPL = KBArticleLocalService.class.getName() +
		".impl";
	private static final String _TX_IMPL = KBArticleLocalService.class.getName() +
		".transaction";
	private static KBArticleLocalServiceFactory _factory;
	private static KBArticleLocalService _impl;
	private static KBArticleLocalService _txImpl;
	private KBArticleLocalService _service;
}