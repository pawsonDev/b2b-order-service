package com.zawadzkidevelop.b2borderservice.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VatNumberSpecification {

    private boolean valid;
    private String database;
    private boolean format_valid;
    private String query;
    private String country_code;
    private String vat_number;
    private String company_name;
    private String company_address;


}
