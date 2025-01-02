//package org.example.models;
//
//import org.bson.codecs.pojo.annotations.BsonCreator;
//import org.bson.codecs.pojo.annotations.BsonProperty;
//import org.example.exceptions.TooManyException;
//import org.example.repositories.MgdClientRepository;
//
//import java.time.LocalDate;
//
//public class Rent extends AbstractEntityMgd {
//
//    @BsonProperty("beginDate")
//    private LocalDate beginDate;
//
//    @BsonProperty("endDate")
//    private LocalDate endDate;
//
//    @BsonProperty("clientId")
//    private UniqueIdMgd clientId;
//
//    @BsonProperty("bookId")
//    private UniqueIdMgd bookId;
//
//    @BsonProperty("fee")
//    private float fee;
//
//    @BsonCreator
//    public Rent(@BsonProperty("clientId") UniqueIdMgd clientId,
//                @BsonProperty("bookId") UniqueIdMgd bookId,
//                @BsonProperty("beginDate") LocalDate beginDate,
//                @BsonProperty("endDate") LocalDate endDate) throws TooManyException {
//        super();
//        this.clientId = clientId;
//        this.bookId = bookId;
//        this.beginDate = beginDate;
//        this.endDate = endDate;
//    }
//
//    public void calculateFee(Client client) {
//        if (client.getClientType() instanceof NonStudent) {
//            this.fee = ((NonStudent) client.getClientType()).getAdditionalFee();
//        } else if (client.getClientType() instanceof Student) {
//            this.fee = 0;
//        }
//    }
//
//    public void returnBook(Client client) {
//        client.removeRent(this);
//    }
//
//    public float getFee() {
//        return fee;
//    }
//
//    public LocalDate getBeginDate() {
//        return beginDate;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public UniqueIdMgd getClientId() {
//        return clientId;
//    }
//
//    public void setFee(float fee) {
//        this.fee = fee;
//    }
//
//    public UniqueIdMgd getBookId() {
//        return bookId;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }
//}
//
