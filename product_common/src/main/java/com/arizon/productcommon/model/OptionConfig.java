/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author sasikumar.a
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionConfig {

    public String default_value;
    public boolean checked_by_default;
    public String checkbox_label;
    public boolean date_limited;
    public String date_limit_mode;
    public String date_earliest_value;
    public String date_latest_value;
    public String file_types_mode;
    public ArrayList<String> file_types_supported;
    public ArrayList<String> file_types_other;
    public int file_max_size;
    public boolean text_characters_limited;
    public int text_min_length;
    public int text_max_length;
    public boolean text_lines_limited;
    public int text_max_lines;
    public boolean number_limited;
    public String number_limit_mode;
    public int number_lowest_value;
    public int number_highest_value;
    public boolean number_integers_only;
    public boolean product_list_adjusts_inventory;
    public boolean product_list_adjusts_pricing;
    public String product_list_shipping_calc;
}
