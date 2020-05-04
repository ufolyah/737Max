package B737Max.Components;

/**
 * Set the two type of seat
 *
 * @author xudufy
 * @version 2.0 2020-05-03
 * @since 2020-03-01
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
