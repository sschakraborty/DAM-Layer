package com.sschakraborty.platform.damlayer.audit.core.creator.resource;

import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

public interface AuditResourceCreator {
    String createResource(Model model);
}