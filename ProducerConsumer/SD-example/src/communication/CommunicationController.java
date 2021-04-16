package communication;

import common.IQuestion;

public class CommunicationController {
    static IQuestion instance = null;

    public static IQuestion getReference() throws Exception {
		if (instance == null) {
			synchronized (CommunicationController.class) {
				if (instance == null) {
					instance = new CommunicationService();
				}
			}
		}

		return instance;
	}
}