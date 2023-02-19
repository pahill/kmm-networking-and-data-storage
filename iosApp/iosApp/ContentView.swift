import SwiftUI
import shared
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesCombine
import KMPNativeCoroutinesAsync

struct ContentView: View {
  @ObservedObject private(set) var viewModel: ViewModel

    var body: some View {
        NavigationView {
            listView()
            .navigationBarTitle("SpaceX Launches")
            .onAppear{ self.viewModel.startObserving()}
        }
    }

    private func listView() -> AnyView {
        switch viewModel.launches {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let launches):
            return AnyView(List(launches) { launch in
                RocketLaunchRow(rocketLaunch: launch)
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {
    
    enum LoadableLaunches {
        case loading
        case result([RocketLaunch])
        case error(String)
    }
    
    @MainActor
    class ViewModel: ObservableObject {
        let sdk: SpaceXSDK
        
        @Published var launches = LoadableLaunches.loading
        
        init(sdk: SpaceXSDK) {
            self.sdk = sdk
        }
        
        func startObserving(){
            self.launches = .loading
            
            Task {
                do {
                    let stream = asyncStream(for: self.sdk.getLaunchesNative())
                    for try await data in stream {
                        self.launches = .result(data)
                    }
                } catch {
                    print("Failed with error: \(error)")
                    self.launches = .error("Error")
                }
            }
        }
    }
}

extension RocketLaunch: Identifiable { }
