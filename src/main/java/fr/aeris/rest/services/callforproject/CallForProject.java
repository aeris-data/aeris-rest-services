package fr.aeris.rest.services.callforproject;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import fr.sedoo.commons.util.StringUtil;

public class CallForProject  {

	private Long id;

	private String name;

	private String context;

	private String comments;

	private String status;

	private String description;

	private String support;

	private String use;

	private Date submissionDate;
	
	private String dueDate;
	
	private String dueDateJustification;

	private String contactsAsString;

	private String humanResources;

	private String computingResources;

	private String directorEmail;

	private String directorName;

	private String applicantEmail;

	private String applicantName;

	private String applicantLaboratory;

	private List<Responsible> responsibles;

	public String getDirectorEmail() {
		return directorEmail;
	}

	public void setDirectorEmail(String directorEmail) {
		this.directorEmail = directorEmail;
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public String getApplicantEmail() {
		return applicantEmail;
	}

	public void setApplicantEmail(String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getApplicantLaboratory() {
		return applicantLaboratory;
	}

	public void setApplicantLaboratory(String applicantLaboratory) {
		this.applicantLaboratory = applicantLaboratory;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactsAsString() {
		return contactsAsString;
	}

	public void setContactsAsString(String contactsAsString) {
		this.contactsAsString = contactsAsString;
	}


	public String getHumanResources() {
		return humanResources;
	}

	public void setHumanResources(String humanResources) {
		this.humanResources = humanResources;
	}

	public String getComputingResources() {
		return computingResources;
	}

	public void setComputingResources(String computingResources) {
		this.computingResources = computingResources;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDueDateJustification() {
		return dueDateJustification;
	}

	public void setDueDateJustification(String dueDateJustification) {
		this.dueDateJustification = dueDateJustification;
	}

	public List<Responsible> getResponsibles() {
		return responsibles;
	}

	public void setResponsibles(List<Responsible> responsibles) {
		this.responsibles = responsibles;
	}

}
