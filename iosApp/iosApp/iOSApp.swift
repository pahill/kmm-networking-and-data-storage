import SwiftUI
import shared

@main
struct iOSApp: App {
    //sdk
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: .init())
        }
    }
}
