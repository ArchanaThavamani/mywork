/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arizon.ordercommons.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author kavinkumar.s
 */
@Entity
@Table(schema = "commonbc",name = "tbl_email_notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OCEmailNotificationTableTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Integer emailID;
    @Column(name = "email_subject")
    private String emailSubject;
    @Column(name = "email_message")
    private String emailMessage;
    @Column(name = "email_content")
    private String emailContent;
    @Column(name = "interface")
    private String interfaceType;
    @Column(name = "notification_mail_id")
    private String notificationMailID;
}
