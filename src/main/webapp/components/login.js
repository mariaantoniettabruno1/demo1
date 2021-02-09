//component per il form di login
let LoginContainer = {
    data: function () {
        return {

            username: '',
            usernameRules: [
                v => !!v || 'Username non può essere vuoto'
            ],
            password: '',
            passwordRules: [
                v => !!v || 'Password non può essere vuota',
                v => v.length <= 8 || 'La password deve avere meno di 8 caratteri',
            ],
            show_password: false,
            ruolo: "",
            azione: "checkACC",
        }
    },
    template: `
        <v-main>
            <v-container>
               <v-form>
                <v-text-field
                    v-model="username"
                    :rules="usernameRules"
                    label="Username"
                    required
                ></v-text-field>
                <v-text-field
                    v-model="password"
                    :rules="passwordRules"
                    :append-icon="show_password ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show_password ? 'text' : 'password'"
                    name="input-10-1"
                    label="Password"
                    hint="Al più 8 caratteri"
                    :counter="8"
                    @click:append="show_password = !show_password"
                    required
                ></v-text-field>  
                <v-btn
                        rounded
                        color="primary"
                        dark                        
                        v-on:click="login"> Login                    
                    </v-btn>            
                </v-form>
            </v-container>
        </v-main>
    `,
    methods: {
        async login() {
            //concetto simile a var self = this, assegnamo this.router cosi da poterlo richiamare in futuro.
            let navigate = this.$router;
            //fetch per la query di login, passando utente, password e l'azione richiesta ( che in questo caso è check account)
            this.ruolo = await fetch(`ModificaServlet?azione=${this.azione}&utente=${this.username}&password=${this.password}`)
                .then(function (response) {
                    return response.json(); // trasformiamo il response che ci viene dato dalla fetch in un oggetto json
                })
                .then(function (response) {
                    return response.trim(); // togliamo i possibili spazi che andrebbero a creare problemi nel controllo del valore successivamente
                });



            if (this.ruolo === 'cliente') {
                myStorage.setItem('ruolo', this.ruolo); // settiamo il ruolo dell'utente nella sessione, per il controllo dei permessi con router
                myStorage.setItem('utente', this.username); // uguale a sopra, ma per il nome utente
                navigate.push({path: '/cliente'});
            } else if (this.ruolo === 'amministratore') {
                myStorage.setItem('ruolo', this.ruolo); // settiamo il ruolo dell'utente nella sessione, per il controllo dei permessi con router
                myStorage.setItem('utente', this.username); // uguale a sopra, ma per il nome utente
                navigate.push({path: '/amministratore'});
            } else {
                alert("Utente non registrato");
            }
        }
    }
}