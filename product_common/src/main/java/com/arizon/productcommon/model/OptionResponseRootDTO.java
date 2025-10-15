/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.productcommon.model;

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

public class OptionResponseRootDTO {
    public OptionData data;
    public OptionMeta meta;
}
