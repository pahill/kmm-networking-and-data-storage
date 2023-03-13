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
                RocketLaunchRow(rocketLaunch: launch, action: {showMoreContent(rocketLaunch: launch)})
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
    
    private func showMoreContent(rocketLaunch: RocketLaunch){
        let showMoreContent: MoreContent = viewModel.getMoreContent(rocketLaunch: rocketLaunch)
        if (showMoreContent is MoreContent.YoutubeContent) { let content = (showMoreContent as? MoreContent.YoutubeContent); openLink(link:  content?.link)}
        else if (showMoreContent is MoreContent.ArticleContent) { let content = (showMoreContent as? MoreContent.ArticleContent); openLink(link: content?.link)}
        else if (showMoreContent is MoreContent.WikipediaContent) { let content = (showMoreContent as? MoreContent.WikipediaContent); openLink(link: content?.link)}
    }
    
    private func openLink(link: String?){
        if let link = URL(string: link!) {
          UIApplication.shared.open(link)
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
    class ViewModel: MainViewModel, ObservableObject {
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
