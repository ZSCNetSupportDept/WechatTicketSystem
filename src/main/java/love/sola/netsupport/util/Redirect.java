package love.sola.netsupport.util;

import com.google.common.net.UrlEscapers;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Redirect {

	private static final String REDIRECT_PAGE = lang("Result_Page");

	private static final int SUCCESS = 1;
	private static final int ERROR = 0;
	private static final int WARNING = -1;
	private static final int NON_WECHAT = 88;

	public static RedirectBuilder success() {
		return new RedirectBuilder(SUCCESS);
	}

	public static RedirectBuilder error() {
		return new RedirectBuilder(ERROR);
	}

	public static class RedirectBuilder {

		private StringBuilder sb;

		RedirectBuilder(int type) {
			sb = new StringBuilder(REDIRECT_PAGE).append("?");
			type(type);
		}

		private RedirectBuilder type(int type) {
			sb.append("type=").append(type).append("&");
			return this;
		}

		public RedirectBuilder msg(String msg) {
			sb.append("msg=").append(i18nThenEscape(msg)).append("&");
			return this;
		}

		public RedirectBuilder title(String title) {
			sb.append("title=").append(i18nThenEscape(title)).append("&");
			return this;
		}

		public RedirectBuilder noButton() {
			sb.append("btn=").append("hide").append("&");
			return this;
		}

		public RedirectBuilder button(String text) {
			sb.append("btn=").append(i18nThenEscape(text)).append("&");
			return this;
		}

		public RedirectBuilder icon(WeUIIcon icon) {
			sb.append("icon=").append(icon.toString()).append("&");
			return this;
		}

		public RedirectBuilder to(String url) {
			sb.append("redirect=").append(i18nThenEscape(url)).append("&");
			return this;
		}

		public void go(HttpServletResponse resp) throws IOException {
			resp.sendRedirect(sb.toString());
		}

		public String toString() {
			return sb.toString();
		}

		private static String i18nThenEscape(String str) {
			return UrlEscapers.urlFragmentEscaper().escape(lang(str));
		}

	}

	public enum WeUIIcon {
		SUCCESS("weui_icon_success"),
		SUCCESS_CIRCLE("weui_icon_success_circle"),
		SUCCESS_NO_CIRCLE("weui_icon_success_no_circle"),
		SUCCESS_SAFE("weui_icon_safe_success"),
		INFO("weui_icon_info"),
		INFO_CIRCLE("weui_icon_info_circle"),
		WAITING("weui_icon_waiting"),
		WAITING_CIRCLE("weui_icon_waiting_circle"),
		CIRCLE("weui_icon_circle"),
		WARN("weui_icon_warn"),
		WARN_SAFE("weui_icon_safe_warn"),
		DOWNLOAD("weui_icon_download"),
		CANCEL("weui_icon_cancel"),
		;

		private String value;

		WeUIIcon(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

	}

}
