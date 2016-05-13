package love.sola.netsupport.util;

import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Ticket;

import java.text.SimpleDateFormat;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class ParseUtil {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");

	public static String parseTicket(Ticket t) {
		StringBuilder sb = new StringBuilder()
				.append(lang("Ticket_Info_Id")).append(t.getId()).append("\n")
				.append(lang("Ticket_Info_Desc")).append(t.getDescription()).append("\n")
				.append(lang("Ticket_Info_Submit_Time")).append(dateFormat.format(t.getSubmitTime())).append("\n");
		if (t.getOperator() != null)
			sb.append(lang("Ticket_Info_Operator")).append(t.getOperator().getId()).append("\n");
		if (t.getRemark() != null) sb.append(lang("Ticket_Info_Remark")).append(t.getRemark()).append("\n");
		if (t.getUpdateTime() != null)
			sb.append(lang("Ticket_Info_Update_Time")).append(dateFormat.format(t.getUpdateTime())).append("\n");
		sb.append(lang("Ticket_Info_Status")).append(Status.getLocalized(t.getStatus()));
		return sb.toString();
	}

}
