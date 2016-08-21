/**
 * Created by David on 20/08/2016.
 */

import Environment
import RCValidator

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
        println("   :: SITES: ${result}")
        return result
    }

    def getURL() {
        String withAttendantsQuery = "and entrada.id in ( select entrada.id from entrada, tipo, evento where entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.attendant_type_id is not null " +
                "union SELECT entrada.id FROM entrada, tipo, evento, categoria WHERE entrada.tipo_id = tipo.id AND tipo.evento_id = evento.id AND evento.categoria_principal_id = categoria.id and categoria.attendant_type_id is not null " +
                "union SELECT entrada.id FROM categoria AS node , categoria AS parent ,entrada, tipo, evento, categoria " +
                "WHERE entrada.tipo_id = tipo.id and tipo.evento_id = evento.id and evento.categoria_principal_id = categoria.id and " +
                "node.lft BETWEEN parent.lft AND parent.rgt AND node.id = evento.categoria_principal_id and categoria.attendant_type_id is not null GROUP BY parent.id) "

        def res = thisEnv.getQuery("Select * from site limit 1")
        println("   :: RESULTADO: ${res}")
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
