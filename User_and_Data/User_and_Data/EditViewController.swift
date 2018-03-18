//
//  EditViewController.swift
//  User_and_Data
//
//  Created by Justin Hinds on 11/8/17.
//  Copyright © 2017 Justin Hinds. All rights reserved.
//

import UIKit
import Firebase
class EditViewController: UIViewController {

    @IBOutlet weak var nameText: UITextField!
    @IBOutlet weak var ageText: UITextField!
    
    
    @IBAction func nameDelete(_ sender: Any) {
        let user = FIRAuth.auth()?.currentUser
        let nameRef = FIRDatabase.database().reference().child("Users").child((user?.uid)!).child("name")
        nameRef.removeValue()
    }
    @IBAction func ageDelete(_ sender: Any) {
        let user = FIRAuth.auth()?.currentUser
        let ageRef = FIRDatabase.database().reference().child("Users").child((user?.uid)!).child("age")
        ageRef.removeValue()
    }
    @IBAction func saveAction(_ sender: Any) {
        let user = FIRAuth.auth()?.currentUser

        if let name = nameText.text, let age = ageText.text{
            let ref = FIRDatabase.database().reference().child("Users")
            if let realAge = NumberFormatter().number(from: age){
                let values = ["name": name, "age": realAge] as [String : Any]
                let childUpdates = ["/\(user?.uid ?? "N/A")": values]
                ref.updateChildValues(childUpdates)

            }
        }
    }
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        observeUserInfo()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

    func observeUserInfo() -> Void {
        let user = FIRAuth.auth()?.currentUser
        if (user) != nil{
            print(FIRAuth.auth()?.currentUser?.email! ?? "n/a")
            let ref = FIRDatabase.database().reference().child("Users").child((user?.uid)!)
            ref.observe(.value, with: { (snapshot) in
                if let dictionary = snapshot.value as? [String : AnyObject]{
                    //let currentUser = User()
                    print(dictionary)
                    //currentUser.setValuesForKeys(dictionary)
                    self.nameText.text = dictionary["name"] as? String ?? "N/A" 
                    self.ageText.text = "\(String(describing: dictionary["age"] ?? "N/A" as AnyObject))"
                    
                }
            })
        }
    }
}
