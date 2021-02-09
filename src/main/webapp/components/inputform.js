let InputformContainer = {
    name: "inputform-container",
    props: ["azione"],
    data : function (){
        return {
        }
    },
    template: `
    <div style="margin-top:2%">
    <div v-if="azione === 'insertDocente'" style="margin-top:10%" >                                               
    <v-form v-model="valid">
      <v-container>
        <v-row>
          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="dato1"
            ></v-text-field>
          </v-col>

          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="dato2"
            ></v-text-field>
          </v-col>

          <v-col
            cols="12"
            md="4"
          >
            <v-text-field
              v-model="dato3"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-container>
    </v-form>
    </div>
    </div>
    `
}