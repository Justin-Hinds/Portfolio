//
//  LoginViewController.swift
//  User_and_Data
//
//  Created by Justin Hinds on 11/7/17.
//  Copyright © 2017 Justin Hinds. All rights reserved.
//

import UIKit
import Firebase
import GoogleSignIn
import SystemConfiguration
class LoginViewController: UIViewController, GIDSignInUIDelegate {

    
 
    @IBOutlet weak var emailText: UITextField!
    @IBOutlet weak var passwordText: UITextField!
    @IBOutlet weak var loginButton: UIButton!
    @IBOutlet weak var createButton: UIButton!
    
    
    let reachability = Reachability()!
    @IBAction func loginAction(_ sender: UIButton) {
        
        if let email = self.emailText.text, let password = self.passwordText.text{
            FIRAuth.auth()?.signIn(withEmail: email, password: password, completion: { (user, error) in
                if let error = error{
                    print("Error Error")
                    return
                }
            self.performSegue(withIdentifier: "loginSegue", sender: AnyObject.self)
            })
        }
    }
    @IBAction func createAccountAction(_ sender: UIButton) {
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        GIDSignIn.sharedInstance().uiDelegate = self

        NotificationCenter.default.addObserver(self, selector: #selector(networkChanged), name: Notification.Name.reachabilityChanged, object: reachability)
        do{
            try reachability.startNotifier()
        }catch{
            
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func networkChanged(noti: Notification){
        let reachability = noti.object as! Reachability
        if reachability.connection != .none{
            print("CONNECTED")
            loginButton.isEnabled = true
            createButton.isEnabled = true
            
        }else{
            print("NOT CONNECTED")
            loginButton.isEnabled = false
            createButton.isEnabled = false
            let alert = UIAlertController(title: "No Connection", message: "Please check your network connection before attempting to login", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }


}
