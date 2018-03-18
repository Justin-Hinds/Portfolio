//
//  CreateAccountViewController.swift
//  User_and_Data
//
//  Created by Justin Hinds on 11/7/17.
//  Copyright © 2017 Justin Hinds. All rights reserved.
//

import UIKit
import Firebase
class CreateAccountViewController: UIViewController {

    @IBOutlet weak var emailText: UITextField!
    @IBOutlet weak var passwordText: UITextField!
    @IBOutlet weak var nameText: UITextField!
    @IBOutlet weak var ageText: UITextField!
    
    @IBAction func createAction(_ sender: Any) {
        if let email = emailText.text, let password = passwordText.text, let name = nameText.text, let age = ageText.text{
            FIRAuth.auth()?.createUser(withEmail: email, password: password, completion: { (user, error) in
                if let error = error{let alert = UIAlertController(title: "Error", message: "Please check your Email address and password", preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    
                    return
                }
                if self.passwordValidate(password: password){
                    self.addFirUser()
                }else{
                    let alert = UIAlertController(title: "Invalid Password", message: "Passwords must have 6 characters, one uppercase letter, one lowercase and a number.", preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    return
                }
            })
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

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
    func addFirUser() -> Void {
       let ref = FIRDatabase.database().reference().child("Users").child((FIRAuth.auth()?.currentUser?.uid)!)
        if let name = nameText.text, let realAge = NumberFormatter().number(from: ageText.text!){
            ref.setValue(["name":name, "age" : realAge])
        }else{
            ref.setValue(["name":"name", "age" : 99])
        }
        let vc = storyboard?.instantiateViewController(withIdentifier: "MainNav")
        present(vc!, animated: true, completion: nil)
        
    }

    func passwordValidate(password: String) -> Bool {
        let validPassword = NSPredicate(format: "SELF MATCHES %@", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,}$")
        return validPassword.evaluate(with:password)
    }
    
}
