let OperazioniContainer = {
    name: "operazioni-container",
    props: ["selected", "operazione"],
    data : function (){
        return {
            nome:"",
            cognome:"",
            materia:"",
            result : null,
        }
    },
    components: { InputformContainer },
    template: `
    <div style="margin-top:2%">
    <div v-if="operazione === 'ripetizioni'" >  
                                              <!--operazioni tabella prenotazioni-->
                    <v-btn                 
                        color="#41A317"
                        elevation="2"
                        raised
                        v-on:click="inserisciPrenotazione"> Prenota
                    </v-btn>
                    </div>                   
     <div v-else-if="operazione === 'prenotazione'">         <!--operazioni tabella cronologiaRipetizioni-->
                    <v-btn             
                        color="#E42217"
                        elevation="2" 
                        raised
                        v-on:click="deletePrenotazione"> Disdici
                    </v-btn>
                    <v-btn                 
                        color="#41A317"
                        elevation="2"
                        raised
                        v-on:click="prenotazioneEffettuata"> Segna come Effettuata
                    </v-btn>
                    
     </div>
     <div v-else-if="operazione === 'docente'">           <!--operazoni tabella docente-->
                    <v-form  id="textbox Docente">
                    <v-container>
                        <v-row>
                            <v-col
                            cols="12"
                            md="4"
                            >
                        <v-text-field
                            v-model="nome"
                            label="nome"
                        ></v-text-field>
                            </v-col>

                            <v-col
                            cols="12"
                            md="4"
                            >
                        <v-text-field
                            v-model="cognome"
                            label="cognome"
                            ></v-text-field>
                            </v-col>
                     </v-row>
                     </v-form>        
                    <v-btn                   
                        color="#E42217"
                        elevation="2"
                       
                        raised
                        v-on:click="deleteDocente"> Elimina 
                    </v-btn>
                     <v-btn type="submit" form="textbox form"                 
                        color="#41A317"
                        elevation="2"
                        
                        raised
                        v-on:click="insertDocente"> Inserisci 
                     </v-btn>
</div>
    
     <div v-else-if="operazione === 'corso'">   
     <v-form  id="textbox corso">
         <v-container>
        <v-row>
          <v-col
            cols="12"
            md="4"
          >     
                <v-text-field
              v-model="materia"
              label="materia"
            ></v-text-field>
          </v-col>
          </v-row>
          </v-form> 
                    <v-btn                   
                        color="#E42217"
                        elevation="2"
                        
                        raised
                        v-on:click="deleteCorso"> Elimina 
                    </v-btn>
                     <v-btn   type = "submit" form ="textbox corso"             
                        color="#41A317"
                        elevation="2"
                        
                        raised
                        v-on:click="insertCorso"> Inserisci 
                    </v-btn>
</div>
     <div v-else-if="operazione === 'Insegna'"> 
      <v-form  id="textbox insegna">
        <v-container>
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
         
            <v-text-field
              v-model="nome"
              label="nome"
              required
            ></v-text-field>
          </v-col>

          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="cognome"
              label="cognome"
              required
            ></v-text-field>
          </v-col>
           <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="materia"
              label="materia"
              required
            ></v-text-field>
          </v-col>
          </v-row>
                     </v-form> 
                    <v-btn                   
                        color="#E42217"
                        elevation="2"
                        raised
                        v-on:click="deleteInsegna"> Elimina 
                    </v-btn>
                     <v-btn  type="submit" form ="textbox insegna"                
                        color="#41A317"
                        elevation="2"
                        raised
                        v-on:click="insertInsegna"> Inserisci 
                    </v-btn>
      </div>
</div>
    `,
    methods:{
        async inserisciPrenotazione() {

            if(this.checkSelected()){
                this.azione = "inserisciPrenotazione";

                let url = "ModificaServlet?azione="+this.azione+"&nome="+this.selected[0].nome+"&cognome="+this.selected[0].cognome+"&materia="+this.selected[0].materia+
                    "&account="+myStorage.getItem('utente')+"&data="+this.selected[0].data+"&ora="+this.selected[0].ora;
                await fetch(url);
                alert("Prenotazione effettuata con successo. Può controllare le prenotazioni attive clickando sul bottone cronologia prenotazioni");
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }
        },

        async deletePrenotazione() {
            if(this.checkSelected()){
                this.azione = "deletePrenotazione";
                let url = "ModificaServlet?azione="+this.azione+"&nome="+this.selected[0].nome+"&cognome="+this.selected[0].cognome+"&materia="+this.selected[0].materia+
                    "&account="+myStorage.getItem('utente')+"&data="+this.selected[0].data+"&ora="+this.selected[0].ora;
                await fetch(url);
                if(this.result == 0){
                    alert("La prenotazione non può essere segnata come disdetta dopo la data della stessa.");
                }
                else{
                    alert("Prenotazione segnata come 'disdetta' con successo.");
                }

            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async prenotazioneEffettuata() {

            if(this.checkSelected()){
                this.azione = "segnaEffettuata";

                let url = "ModificaServlet?azione="+this.azione+"&nome="+this.selected[0].nome+"&cognome="+this.selected[0].cognome+"&materia="+this.selected[0].materia+
                    "&account="+myStorage.getItem('utente')+"&data="+this.selected[0].data+"&ora="+this.selected[0].ora;
                await fetch(url)
                    .then(response => response.json())
                    .then(data => this.result = data);

                if(this.result == 0){
                    alert("La prenotazione non può essere segnata come effettuata prima della data della stessa.");
                }
                else{
                    alert("Prenotazione segnata come 'effettuata' con successo.");
                }
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async insertDocente() {
            if(this.checkSelected()){
                this.azione = "inserisciDocente";
                let url = "ModificaServlet?azione="+this.azione+"&nome="+this.nome+"&cognome="+this.cognome;
                await fetch(url);
                alert("Docente inserito con successo dal database.");
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async deleteDocente() {
            if(this.checkSelected()){
                this.azione = "deleteDocente";
                let url = "ModificaServlet?azione="+this.azione+"&nome="+this.selected[0].nome+"&cognome="+this.selected[0].cognome;
                await fetch(url);
                alert("Docente rimosso con successo dal database.");
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async insertCorso() {
            if(this.checkSelected()){
                this.azione = "inserisciCorso";
                let url = "ModificaServlet?azione="+this.azione+"&materia="+this.materia;
                await fetch(url)
                    .then(response => response.json())
                    .then(data => this.result = data);

                if(this.result == 0){
                    alert("Il corso esiste già nel database.");
                }
                else{
                    alert("Corso inserito con successo dal database.");
                };
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async deleteCorso() {
            if(this.checkSelected()){
                this.azione = "deleteCorso";
                let url = "ModificaServlet?azione="+this.azione+"&materia="+this.selected[0].Materia;
                await fetch(url);
                alert("Corso rimosso con successo dal database.");
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async insertInsegna() {
            if(this.checkSelected()) {
                this.azione = "inserisciInsegna";
                let url = "ModificaServlet?azione=" + this.azione + "&nome=" + this.nome + "&cognome=" + this.cognome + "&materia=" + this.materia;
                await fetch(url)
                    .then(response => response.json())
                    .then(data => this.result = data);

                if (this.result == 0) {
                    alert("Docente e/o corso non presenti nel database.");
                } else {
                    alert("Docente e Materia inseriti con successo dal database.");
                }
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }

        },

        async deleteInsegna() {
            if(this.checkSelected()) {
                this.azione = "deleteInsegna";
                let url = "ModificaServlet?azione=" + this.azione + "&nome=" + this.selected[0].nome + "&cognome=" + this.selected[0].cognome + "&materia=" + this.selected[0].materia;
                await fetch(url);
                alert("Coppia Docente-Corso rimossi con successo dal database.");
            }
            else{
                alert("Per eseguire quest'azione devi selezionare una riga della tabella.");
            }
        },

        checkSelected(){
            let controllo = true;

            if(this.selected.length==0){
                controllo = false;
            }
            return controllo;
        }
    }
}