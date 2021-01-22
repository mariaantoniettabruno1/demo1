//component per il form di login
let LoginContainer = {
    data: function () {
        return {

            username: '',
            usernameRules: [
                v => !!v || 'username is required'
            ],
            password: '',
            passwordRules: [
                v => !!v || 'password is required',
                v => v.length <= 8 || 'password must be less than 8 characters',
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
                    hint="At least 8 characters"
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

            let navigate = this.$router;
            this.ruolo = await fetch(`ModificaServlet?azione=${this.azione}&utente=${this.username}&password=${this.password}`)
                .then(function (response) {
                    return response.json()
                })
                .then(function (response) {
                    return response.trim()
                });
            if (this.ruolo === 'cliente') {
                navigate.push({path: '/cliente'});
            } else if (this.ruolo === 'amministratore') {
                navigate.push({path: '/amministratore'});
            } else {
                alert("Utente non registrato");
            }
        }
    }
}