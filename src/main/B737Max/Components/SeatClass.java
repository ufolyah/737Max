package B737Max.Components;

/**
 *
 */
public enum SeatClass {
    FIRST {
        /**
         * @return "FirstClass"
         */
        @Override
        public String toString() {
            return "FirstClass";
        }
    },
    COACH {
        /**
         * @return "Coach"
         */
        @Override
        public String toString() {
            return "Coach";
        }
    }
}
