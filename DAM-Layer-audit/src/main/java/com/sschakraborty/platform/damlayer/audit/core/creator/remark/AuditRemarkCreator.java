package com.sschakraborty.platform.damlayer.audit.core.creator.remark;

import com.sschakraborty.platform.damlayer.shared.audit.DataOperation;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

public interface AuditRemarkCreator {
    String createRemark(DataOperation dataOperation, Model model, boolean success);
}