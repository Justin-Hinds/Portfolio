//
//  ViewController.swift
//  User_and_Data
//
//  Created by Justin Hinds on 11/5/17.
//  Copyright © 2017 Justin Hinds. All rights reserved.
//

import UIKit
import Firebase
import SystemConfiguration
class MainViewController: UIViewController {

    @IBOutlet weak var nameText: UILabel!
    let reachability = Reachability()!
    @IBAction func logoutAction(_ sender: Any) {
        
        let firebaseAuth = FIRAuth.auth()
        do {
            try firebaseAuth?.signOut()
            let vc = storyboard?.instantiateViewController(withIdentifier: "Login")
            present(vc!, animated: true, completion: nil)
        } catch let signOutError as NSError {
            print ("Error signing out: %@", signOutError)
        }
    }
    @IBOutlet weak var ageText: UILabel!
    //let ref = FIRDatabase.database().reference()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        FIRDatabase.database().persistenceEnabled = true

        NotificationCenter.default.addObserver(self, selector: #selector(networkChanged), name: Notification.Name.reachabilityChanged, object: reachability)
        do{
            try reachability.startNotifier()
        }catch{
            
        }
    }
    override func viewDidAppear(_ animated: Bool) {

        let user = FIRAuth.auth()?.currentUser
        if (user) != nil{
            print(FIRAuth.auth()?.currentUser?.email! ?? "n/a")
            let userRef = FIRDatabase.database().reference().child("Users").child((user?.uid)!)
            userRef.keepSynced(true)
            userRef.observe(.value, with: { (snapshot) in
                if let dictionary = snapshot.value as? [String : AnyObject]{
                    //let currentUser = User()
                    print(dictionary)
                    //currentUser.setValuesForKeys(dictionary)
                    self.nameText.text = dictionary["name"] as? String ?? "N/A"
                    self.ageText.text = "\(String(describing: dictionary["age"] ?? "N/A" as AnyObject))"                 }
            })
            
        }else{
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "Login")
            present(vc!, animated: true, completion:  nil)
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
        }else{
            print("NOT CONNECTED")

        }
    }
}

