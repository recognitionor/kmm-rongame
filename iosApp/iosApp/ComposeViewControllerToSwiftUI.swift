//
//  ComposeViewControllerToSwiftUI.swift
//  iosApp
//
//  Created by NHN on 2/26/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ComposeViewControllerToSwiftUI: UIViewControllerRepresentable {
    private let topSafeArea: Float
    private let bottomSafeArea: Float
    
    init(topSafeArea: Float, bottomSafeArea: Float) {
        self.topSafeArea = topSafeArea
        self.bottomSafeArea = bottomSafeArea
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        return Main_iosKt.MainViewController(
            topSafeArea: topSafeArea,
            bottomSafeArea: bottomSafeArea
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

