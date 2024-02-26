import SwiftUI
import FirebaseCore

@main
struct iOSApp: App {
    @Environment(\.scenePhase) private var scenePhase
    init() {
        FirebaseApp.configure()
    }
    
	var body: some Scene {
		WindowGroup {
		           GeometryReader { geo in
                                 ComposeViewControllerToSwiftUI(
                                     topSafeArea: Float(geo.safeAreaInsets.top),
                                     bottomSafeArea: Float(geo.safeAreaInsets.bottom)
                                 )
                                 .ignoresSafeArea()
                                 .onTapGesture {
                                     // Hide keyboard on tap outside of TextField
                                     UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                                 }
                             }

		}
	}
}
