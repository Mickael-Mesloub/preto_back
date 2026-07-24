package fr.preto_back.domains.resa;

public enum ReservationRequestDecision {
    APPROVED,
    DECLINED;

    public ReservationRequestStatus toStatus() {
        return this == APPROVED ? ReservationRequestStatus.APPROVED : ReservationRequestStatus.DECLINED;
    }
}
