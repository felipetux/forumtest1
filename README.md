api_ef_operation
====================

Descripción de que es lo que hace el servicio. Una resumen de cual es la principal funcionalidad.
Obtiene todos las operaciones según el filtro informado, obtiene el detalle de una operación, actualiza estados de la operación.
- - -
	
Listado de operaciones y su descripción

1. ../api/ef/operations/v1/? GET : Obtiene la lista de operaciones.
2. ../api/ef/operations/v1/{contractNumber} GET : Obtiene el detalle de una operacion.
3. ../api/ef/operations/v1/status/{contractNumber} PUT : Actualiza el estado de una operación.
4. ../api/ef/operations/v1/document/{contractNumber}/{documentType} PUT : Actualiza los documentos de pago, segun la validacion del usuario.
5. ../api/ef/operations/v1/payment/{contractNumber} PUT : Actualiza el estado del pago de una operación.
6. ../api/ef/operations/v1/amounts/{dealerDocumentNumber} GET : Obtiene totalizadores de montos, según rut distribuidor.
7. ../api/ef/operations/v1/prepay/{contractNumber} PUT : Actualiza monto prepago para una operacion.


## Descripción archivo YML

	 spfind:Nombre del SP para encontrar todos los valores de una o varias operaciones de la tabla tOperacionGFE.
    statusurl:url del servicio para obtener la parametria del catalogo EAF.
    securityurl: URL del servicio que valida el objeto de seguridad.
    obj: objeto de seguridad.
    spfind: Nombre del SP para buscar registros en tabla tOperacionGFE.
    spfindproc: Nombre del SP para encontrar todos los valores del proceso de la operacion de la tabla tProcesoOperacionGFE.
    speaf: Nombre del SP para encontrar todos los valores de los elementos del carrito de un contrato.
    spdocumensoli: Nombre del SP para encontrar todos los valores de los documentos solicitados de un contrato.
    spfindinter: Nombre del SP para encontrar todos los valores de los intervinientes de un contrato.
    spdocpago: Nombre del SP para encontrar todos los valores de los documentos de pago de un contrato.
    spfindinter: Nombre del SP para encontrar todos los intervinientes de un contrato.
    spupdate: Nombre del SP que actualiza los datos de un contrato.
    spinsertproc: Nombre del SP que genera un registro en la tabla proceso.
    spupdatedocu: Nombre del SP que actualiza los documentos de StorBox.
    spamounts: Nombre del SP que calcula montos segun estado de operacion y rut distribuidor.
    spdocparam: Nombre del SP que busca la parametria de documentos.
- -  -

## JSON's con entradas y salidas para cada operación


#### GET /api/ef/operations/

JSON entrada 

Params
categoryCode:String,
clientDocumentNumber:String
dealerDocumentNumber:String
dealerName:String
contractNumber:String
operationStatusCode:String
processUser:String
payDateFrom:String
payDateTo:String
pagareDateFrom:String
pagareDateTo:String	
pageNumber:Integer
maxRows:Integer


JSON salida
{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": body
}
	
Datos prueba:

http://localhost:8403/api/ef/operations/v1/?operationStatusCode=EST01&pageNumber=1&maxRows=10

Params
operationStatusCode:String,
pageNumber:Integer
maxRows:Integer

{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": [
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "COMERCIAL AUTOMOTRIZ MIRANDA S.P.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB020543",
            "rut_cliente": "10526322-8",
            "monto_total": 4306505,
            "asignacion": "",
            "fecha_curse": "2018-12-07",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "KIA CHILE S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB027534",
            "rut_cliente": "10392420-0",
            "monto_total": 1733559,
            "asignacion": "",
            "fecha_curse": "2018-11-26",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "AUTOMOTRIZ CORDILLERA S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB030779",
            "rut_cliente": "13242944-8",
            "monto_total": 6885963,
            "asignacion": "",
            "fecha_curse": "2018-12-05",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "KIA CHILE S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB031799",
            "rut_cliente": "18985695-4",
            "monto_total": 1550000,
            "asignacion": "",
            "fecha_curse": "2018-12-07",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "KIA CHILE S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB031921",
            "rut_cliente": "26227423-3",
            "monto_total": 5881700,
            "asignacion": "",
            "fecha_curse": "2018-12-06",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "PIAMONTE S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB032610",
            "rut_cliente": "22317789-1",
            "monto_total": 2805533,
            "asignacion": "",
            "fecha_curse": "2018-12-10",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "KIA CHILE S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB033190",
            "rut_cliente": "12924452-6",
            "monto_total": 4802200,
            "asignacion": "",
            "fecha_curse": "2018-12-13",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "AUTOMOTORA INALCO S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB033259",
            "rut_cliente": "15316225-5",
            "monto_total": 6264044,
            "asignacion": "",
            "fecha_curse": "2018-12-12",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "KIA CHILE S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB033645",
            "rut_cliente": "17834044-1",
            "monto_total": 4250000,
            "asignacion": "",
            "fecha_curse": "2018-12-13",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "descripcion_categoria": "Plata",
            "descripcion_distribuidor": "AUTOMOTORES FRANCO CHILENA S.A.",
            "descripcion_estado": "Disponible",
            "numero_contrato": "LB033680",
            "rut_cliente": "14061534-K",
            "monto_total": 3490000,
            "asignacion": "",
            "fecha_curse": "2018-12-13",
            "fecha_vencimiento": "",
            "fecha_bloqueo": "",
            "dias_pendientes": 0,
            "codigo_estado": "EST01"
        },
        {
            "paginacion": true,
            "paginaSiguiente": 2
        }
    ]
}

#### GET /api/ef/operations/operation/{contractNumber}

JSON entrada 

Headers 
usuario: String

Path Variable
contractNumber:String

JSON salida
{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": body
}
Datos prueba:
http://localhost:8403/api/ef/operations/v1/operation/LB020543

{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "detalle": [
            {
                "numero_contrato": "LB020543",
                "descripcion_categoria": "Sin Categoria",
                "descripcion_estado": "Cargado",
                "numero_operacion": "1618988471",
                "numero_detalle_operacion": "1618988472",
                "rut_cliente": "10526322-8",
                "nombre_cliente": "Shanyce Matta Rempe",
                "email_cliente": "Margo.Malagon@LescoInc.com",
                "fono_cliente": "",
                "direccion_cliente": "",
                "rut_distribuidor": "76067555-5",
                "razon_social_distribuidor": "COMERCIAL AUTOMOTRIZ MIRANDA S.P.A.",
                "nombre_distribuidor": "COMERCIAL AUTOMOTRIZ MIRANDA",
                "descripcion_marca_vehiculo": "KIA MOTORS",
                "descripcion_modelo_vehiculo": "1584 EX 1.4L 6 MT SPORT",
                "anio_vehiculo": 2016,
                "descripcion_version_vehiculo": "RIO 5",
                "descripcion_estado_vehiculo": "USADO",
                "dias": "0",
                "fecha_pago": "1900-01-01",
                "fecha_pagare": "2018-12-07",
                "fecha_primera_cuota": "2019-01-15",
                "fecha_ultima_cuota": "2019-08-15",
                "tasa": "2.00",
                "numero_cuota": 8,
                "monto_cuota_base": 589343,
                "monto_total_cheque": 4327835,
                "monto_gastos_inscripcion": 21330,
                "monto_saldo_precio": 4306505,
                "monto_descuento_prepago": 0,
                "contrato_descuento_prepago": "",
                "producto": "REFINAN C.I. X C.C.",
                "elementos_adicionales": "NO",
                "marca_descarte": "SI",
                "motivo_descarte": "La promocion contiene palabra *REFIN*",
                "monto_paridad_uf": "27558",
                "codigo_tipo_distribuidor": "806952127",
                "cantidad_bienes": "1",
                "precio_venta_unitario": 16290000,
                "precio_venta_total": 16290000,                
                "usuario": "ADMIN   ",
                "nombre_usuario": "ADMINISTRADOR                                                                                                                                                                                           "
            }
        ],
        "intervinientes": [
            {
                "rut_interviniente": "10526322-8 ",
                "nombre_interviniente": "Shanyce Matta Rempe",
                "tipo_interviniente": "TITULAR                       "
            },
            {
                "rut_interviniente": "11452748-3 ",
                "nombre_interviniente": "Shanise Bellettiere Bhatti",
                "tipo_interviniente": "AVAL Y COMPRA PARA            "
            }
        ],
        "elementosCarrito": [],
        "documentosSolicitados": [
            {
                "nombre_documento": "LIQUIDACION DE SUELDO",
                "marca_enviado": "NO",
                "marca_aprobado": "NO",
                "observacion": "3x M$775"
            },
            {
                "nombre_documento": "CEDULA DE IDENTIDAD",
                "marca_enviado": "NO",
                "marca_aprobado": "NO",
                "observacion": ""
            },
            {
                "nombre_documento": "OTRO",
                "marca_enviado": "NO",
                "marca_aprobado": "NO",
                "observacion": "Ref LA355269"
            },
            {
                "nombre_documento": "REFERENCIAS PERSONALES",
                "marca_enviado": "NO",
                "marca_aprobado": "NO",
                "observacion": ""
            }
        ],
        "documentosPagos": []
    }
}

#### PUT /api/ef/operations/v1/status/{contractNumber}

JSON entrada 

Headers 
Content-Type= application/json
usuario: String

Path Variable
contractNumber:String

Body
codigoEstadoOperacion:Integer
descripcionOperacion:String
usuarioModificacion:String
nombreUsuarioModificacion:String


JSON salida
{
    "status": {
        "code": "201",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "retorno": "0"
    }
}
	
Datos prueba:

http://localhost:8403/api/ef/operations/v1/status/LB035859

Headers 
Content-Type= application/json
usuario=48002565


Body
{
	"codigoEstadoOperacion":17,
	"descripcionOperacion":"Asignado",
	"usuarioModificacion":"XH12092",
	"nombreUsuarioModificacion":"ADMIN"
}

{
    "status": {
        "code": "201",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "retorno": "0"
    }
}

#### PUT /api/ef/operations/v1/document/{contractNumber}/{documentType}

JSON entrada 

Headers 
Content-Type= application/json
usuario: String

Path Variable
contractNumber:String
documentType:Integer

Body
recepcionadoUsuario:String
usuarioModificacion:String


JSON salida
{
    "status": {
        "code": "201",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "retorno": "0"
    }
}
	
Datos prueba:

http://localhost:8403/api/ef/operations/v1/document/LB020543/24

Headers 
Content-Type= application/json
usuario=48002565

Path Variable
contractNumber:String
documentType:Integer


Body
{
	"recepcionadoUsuario":"SI",
	"usuarioModificacion":"XH001732"
}

{
    "status": {
        "code": "201",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "retorno": "0"
    }
}

#### PUT /api/ef/operations/v1/payment/{contractNumber}

JSON entrada 

Headers 
Content-Type= application/json
usuario: String

Path Variable
contractNumber:String

Body
codigoEstadoOperacion:Integer
descripcionOperacion:String
usuarioModificacion:String
nombreUsuarioModificacion:String
dias:Integer


JSON salida
{
    "status": {
        "code": "201",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "retorno": "0"
    }
}
	
Datos prueba:

http://localhost:8403/api/ef/operations/v1/payment/LB037632

Headers 
Content-Type= application/json
usuario=48002565

Path Variable
contractNumber:String


Body
{
    "codigoEstadoOperacion":19,
    "descripcionOperacion":"Anticipado",
    "usuarioModificacion":"XH12092",
    "nombreUsuarioModificacion":"AISOTO",
    "dias":40
}

{
    "status": {
        "code": "201",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "retorno": "0"
    }
}


#### GET /api/ef/operation/amounts/{dealerDocumentNumber}

JSON entrada 

Headers 
Content-Type= application/json
usuario: String

Path Variable
dealerDocumentNumber:String


JSON salida
{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": body
}
	
Datos prueba:

http://localhost:8403/api/ef/operations/v1/payment/LB037632

Headers 
Content-Type= application/json
usuario=48002565


{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "montoTotalDisponibles": 8745329,
        "montoTotalBloqueados": 23745075,
        "montoTotalVencidos": 16824938,
        "montoTramo1": 5975791,
        "montoTramo2": 9029237,
        "montoTramo3": 1673870
    }
}


#### GET /api/ef/operation/prepay/{contractNumber}

JSON entrada 

Headers 
Content-Type= application/json
usuario: String

Path Variable
contractNumber:String

Body entrada
{
"amountPrepago":9911,
"usuarioModificacion":"XH12092",
"nombreUsuarioModificacion":"JUAN PEREZ",
"montoSaldoPrecio":13148100,
"montoGastoInscripcion":62860,
"marcaCarrito":"SI",
"contractPrepago":"LB185629"
}

JSON salida
{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": body
}
	
Datos prueba:

http://localhost:8403/api/ef/operations/v1/amounts/1-9

Headers 
Content-Type= application/json
usuario=48002565


{
    "status": {
        "code": "200",
        "message": "Respuesta exitosa"
    },
    "result": true,
    "data": {
        "montoTotalDisponibles": 8745329,
        "montoTotalBloqueados": 23745075,
        "montoTotalVencidos": 16824938,
        "montoTramo1": 5975791,
        "montoTramo2": 9029237,
        "montoTramo3": 1673870
    }
}


Comentarios adicionales en relación al servcio como alguna configuración o consideración adicional.