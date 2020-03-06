package B737Max.Components;

public enum SeatClass {
    FIRST {
        @Override
        public String toString() {
            return "FirstClass";
        }
    },
    COACH {
        @Override
        public String toString() {
            return "Coach";
        }
    }
}
