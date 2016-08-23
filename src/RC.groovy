/**
 * Created by David on 20/08/2016.
 */

import Environment
import RCValidator

import javax.swing.text.StyledEditorKit.BoldAction

class RC {

    def thisEnv = Environment.getInstance()

    def start() {
        Environment entorno = Environment.createInstance(environment: Env.QA)
        RCValidator rcValidator = new RCValidator()
        rcValidator.main()
    }

    String[] getSitesList(){
        String query = """SELECT site.domain FROM site ORDER BY site.id """
        def res = thisEnv.getQuery(query)
        String[] result = res.findAll().collect() {it.domain}
        //println("   :: SITES: ${result}")
        return result
    }

    String getURL(int ticketType, Boolean pum, Boolean lar, Boolean withAttendants, Boolean limiteInt, String domain) {

        String limiteInternacionalQuery = ""
        if(limiteInt) {
            limiteInternacionalQuery = " and entrada.fecha_limite_internacional < now() "
        }else{
            limiteInternacionalQuery = " and entrada.fecha_limite_internacional >= now() "
        }

        String ticketTypeQuery = ""
        if (ticketType == 0 || ticketType == 2) {
            ticketTypeQuery = " and entrada.tipo_entrada = ${ticketType} "
        }

        String pumQuery = ""
        if (pum) {
            pumQuery = " and (entrada.pum = true or evento.fecha_ultimo_minuto <= now()) "
        }else {
            pumQuery = " and entrada.pum = false and evento.fecha_ultimo_minuto > now()"
        }

        String larQuery = ""
        if (lar) {
            larQuery = """ and (evento.local_address_required = true or exists (SELECT parent.local_address_required FROM categoria AS node, categoria AS parent
                WHERE node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id and parent.local_address_required = true GROUP BY parent.id)) """
        }else {
            larQuery = """ and (evento.local_address_required = false and not exists (SELECT parent.local_address_required FROM categoria AS node, categoria AS parent
                WHERE node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id and parent.local_address_required = true GROUP BY parent.id)) """
        }

        String withAttendantsQuery = ""

        if (withAttendants) {
            withAttendantsQuery =" and entrada.id in ( select entrada.id from entrada, tipo, evento where entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.attendant_type_id is not null " +
                    "union SELECT entrada.id FROM entrada, tipo, evento, categoria WHERE entrada.tipo_id = tipo.id AND tipo.evento_id = evento.id AND evento.categoria_principal_id = categoria.id and categoria.attendant_type_id is not null " +
                    "union SELECT entrada.id FROM categoria AS node , categoria AS parent ,entrada, tipo, evento, categoria " +
                    "WHERE entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.categoria_principal_id = categoria.id and " +
                    "node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id and categoria.attendant_type_id is not null GROUP BY parent.id) "
        }else{
            withAttendantsQuery = " and entrada.id NOT in ( select entrada.id from entrada, tipo, evento where entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.attendant_type_id is not null " +
                    "union SELECT entrada.id FROM entrada, tipo, evento, categoria WHERE entrada.tipo_id = tipo.id AND tipo.evento_id = evento.id AND evento.categoria_principal_id = categoria.id and categoria.attendant_type_id is not null " +
                    "union SELECT entrada.id FROM categoria AS node , categoria AS parent ,entrada, tipo, evento, categoria " +
                    "WHERE entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.categoria_principal_id = categoria.id and " +
                    "node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id and categoria.attendant_type_id is not null GROUP BY parent.id) "
        }

        String query = """select entrada.id from entrada, tipo, evento where entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and entrada.estado = 0
                    and entrada.cantidad_disponible > 0 and evento.date >= now() ${ticketTypeQuery} ${limiteInternacionalQuery} ${larQuery} ${withAttendantsQuery} ${pumQuery} ORDER BY entrada.id LIMIT 200 """

        println("   :: QUERY: ${query}")
        def res = thisEnv.getQuery(query)
        println("   :: RESULTADO: ${res}")

        String url = ""
        if (!res.isEmpty()){
            Collections.shuffle(res)
            def ticketId = res[0]['id']
            url = "https://${domain}/checkout/buy/${ticketId}"
        } else {
            url = "NO SE HAN ENCONTRADO ENTRADAS..."
        }
        println("   :: URL: ${url}")
        return url
    }

    String getReportTemplate(String url, Map<String,String> condiciones){
        String template ="""Site:  {CODE}{CODE}
Event: {CODE}{CODE}
User:  {CODE}{CODE}
Result: {CODE}{CODE}
Expected result:   {CODE}{CODE}

How to reproduce the error:

Step 1: {CODE}{CODE}
Step 2: {CODE}{CODE}
Step 3: {CODE}{CODE}"""

    }

    /*
    static def findTicketId(int type, int num = 1, boolean withAttendants = false) {
        String query = "select entrada.id from entrada, tipo, evento where entrada.tipo_id = tipo.id " +
                "and tipo.evento_id = evento.id and entrada.estado = 0 and entrada.cantidad_disponible > 0 " +
                "and entrada.pum = false and entrada.tipo_entrada = ${type.type} and " +
                "entrada.fecha_limite_internacional >= now() and evento.fecha_ultimo_minuto >= now() " +
                "and evento.date >= now()and evento.local_address_required = false " +
                "and not exists (SELECT parent.local_address_required FROM categoria AS node, categoria AS parent " +
                "WHERE node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id " +
                "and parent.local_address_required = true GROUP BY parent.id) "

        String withAttendantsQuery = "and entrada.id in ( select entrada.id from entrada, tipo, evento where entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.attendant_type_id is not null " +
                "union SELECT entrada.id FROM entrada, tipo, evento, categoria WHERE entrada.tipo_id = tipo.id AND tipo.evento_id = evento.id AND evento.categoria_principal_id = categoria.id and categoria.attendant_type_id is not null " +
                "union SELECT entrada.id FROM categoria AS node , categoria AS parent ,entrada, tipo, evento, categoria " +
                "WHERE entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.categoria_principal_id = categoria.id and " +
                "node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id and categoria.attendant_type_id is not null GROUP BY parent.id) "

        query = withAttendants ? query + withAttendantsQuery + "limit 1000" : query + "limit 1000"
        def res = env.getQuery(query)
        Collections.shuffle(res)
        return res[0..num - 1]["id"]
    }

    static def findTicketIdPum(int num = 1) {
        String query = "select entrada.id from entrada, tipo, evento where \
    		entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and \
    		evento.date >= now() and evento.fecha_ultimo_minuto <= now() and \
    		entrada.available = 1 and entrada.cantidad_disponible > 0"
        def res = env.getQuery(query)
        Collections.shuffle(res)
        return res[0..num - 1]["id"]
    }

    static def findTicketIdLar(int num = 1) {
        String query = "SELECT en.id FROM categoria AS node, categoria AS \
        parent, evento ev, tipo ti, entrada en WHERE node.lft BETWEEN \
        parent.lft AND parent.rgt AND node.id IN (SELECT categoria_id FROM \
        evento_categorias AS ec WHERE ec.evento_id = ev.id) AND \
        parent.local_address_required IS TRUE AND ev.id = ti.evento_id AND \
        ti.id = en.tipo_id AND en.estado = 0 AND en.cantidad_disponible > 0 \
        AND en.tipo_entrada = 0 AND en.fecha_limite_internacional >= NOW() \
        AND ev.fecha_ultimo_minuto >= NOW() AND ev.date >= NOW() limit 10 "
        def res = env.getQuery(query)
        Collections.shuffle(res)
        return res[0..num - 1]["id"]
    }
    */

}
