/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_classes;

/**
 *
 * @author gianma
 */
public class Notification {
    private int id;
    private int notified_id;
    private int notifier_id;
    private int type;
    private String description;
    private int review_id;
    private int photo_id;
    private boolean validated;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the notified_id
     */
    public int getNotified_id() {
        return notified_id;
    }

    /**
     * @param notified_id the notified_id to set
     */
    public void setNotified_id(int notified_id) {
        this.notified_id = notified_id;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the review_id
     */
    public int getReview_id() {
        return review_id;
    }

    /**
     * @param review_id the review_id to set
     */
    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    /**
     * @return the photo_id
     */
    public int getPhoto_id() {
        return photo_id;
    }

    /**
     * @param photo_id the photo_id to set
     */
    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    /**
     * @return the validated
     */
    public boolean isValidated() {
        return validated;
    }

    /**
     * @param validated the validated to set
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    /**
     * @return the notifier_id
     */
    public int getNotifier_id() {
        return notifier_id;
    }

    /**
     * @param notifier_id the notifier_id to set
     */
    public void setNotifier_id(int notifier_id) {
        this.notifier_id = notifier_id;
    }
    
}
