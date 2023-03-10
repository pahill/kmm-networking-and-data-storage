import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
            InitHelperKt.doInitKoinApp()
    }
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: .init(sdk: SDKHelper()))
        }
    }
}
