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
            .onAppear{ self.viewModel.startObservingStuff()}
            .onDisappear {self.viewModel.stopObservingStuff()}
            .navigationBarItems(trailing:
                Button("Reload") {
                    //self.viewModel.loadLaunches()
            })
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
        
        var fetchStationsTask: Any? = nil
        
        @Published var launches = LoadableLaunches.loading
        
        init(sdk: SpaceXSDK) {
            self.sdk = sdk
            //self.loadLaunches()
        }
        
        
        func startObservingStuff(){
            print("start observing")
            self.launches = .loading
            
            Task {
                do {
                    let stream = asyncStream(for: self.sdk.getLaunchesNative())
                    for try await data in stream {
                        data({rocketLaunch, something in
                            //var b = Array<RocketLaunch>()
                            self.launches = .result(rocketLaunch)
                            return KotlinUnit()
                            
                        },{error, something in
                            print(error)
                            self.launches = .error(error.debugDescription)
                            return KotlinUnit()
                        })()
                    }
                } catch {
                    print("Failed with error: \(error)")
                }
            }
//            let publisher = createPublisher(for: self.sdk.getLaunchesNative())
//            print("created the observer")
//            // Now use this publisher as you would any other
//            let cancellable = publisher.sink { completion in
//                print("Received completion: \(completion)")
//            } receiveValue: { value in
//                print("Received value: \(value)")
//
//                self.launches = .result(value)
//            }
//            print("after")

            // To cancel the flow (collection) just cancel the publisher
            //cancellable.cancel()
        }
        
        func stopObservingStuff(){
            print("stop obs")
            //fetchStationsTask?.cancel()
        }
        
    }
}

extension RocketLaunch: Identifiable { }

//func loadLaunches() {
//            self.launches = .loading
//
//            fetchStationsTask = Task {
//                 do {
//                     let stream = asyncStream(for: self.sdk.getLaunchesNative())
//                     for try await data in stream {
//                         print(data)
//                         data({rocketLaunch, something in
//                             //var b = Array<RocketLaunch>()
//                             self.launches = .result(rocketLaunch)
//                             return KotlinUnit()
//                             //                          let i = rocketLaunch.iterator()
//                             //                          while (i.hasNext()){
//                             //                              let r = i.next() as! RocketLaunch
//                             //                              b.append(r)
//                             //                          }
//                             //
//                             //                          self.launches = .result(b)
//                             //
//                         },{error, something in
//                             print(error)
//                             self.launches = .error(error.debugDescription)
//                             return KotlinUnit()
//                         })()
//                     }
//                 } catch {
//                     print("Failed with error: \(error)")
//                 }
//             }
//        }
