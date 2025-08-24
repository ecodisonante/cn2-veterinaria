# CLINICA VETERINARIA

## Componentes

1. **Frontend**
   Corresponde a la interfaz web o móvil utilizada por clientes y empleados. Se comunica con el sistema a través del API Gateway, enviando y recibiendo datos. Su autenticación se gestiona mediante el servicio IDaaS.

2. **IDaaS (Identity as a Service)**
   Servicio externo de autenticación y autorización. Emite tokens y roles (cliente, médico, administrador) que luego son validados por los demás componentes del sistema. Asegura un único punto de gestión de identidades.

3. **API Gateway**
   Punto de entrada único al backend. Valida los tokens entregados por IDaaS, aplica seguridad básica (rate limit, CORS) y redirige las llamadas hacia el BFF o las funciones serverless.

4. **BFF (Backend for Frontend)**
   Microservicio encargado de adaptar y simplificar las respuestas de los microservicios y funciones hacia el frontend. Se enfoca en consultas y agregaciones, no en lógica de negocio.

5. **Funciones Serverless (FaaS)**

   * **Cliente:** funciones que implementan el CRUD del perfil de cliente.
   * **Mascota:** funciones que implementan el CRUD de las mascotas asociadas al cliente.
   * **Reservas:** funciones para crear, modificar o cancelar reservas de citas.
   * **Empleados:** funciones CRUD para gestionar perfiles de empleados, altas y bajas.
   * **Integra Examen:** función que recibe resultados desde laboratorios externos vía webhook, guarda el archivo en Storage y actualiza la base de datos.
   * **Notificaciones:** función que envía mensajes (email, SMS, WhatsApp) a clientes frente a eventos como reservas creadas, resultados recibidos o facturación.

6. **Microservicios (MS en IaaS o contenedores)**

   * **Agenda:** gestiona la disponibilidad de horarios, recalcula al recibir eventos de reservas.
   * **Atención:** registra las consultas médicas, incluyendo diagnósticos, tratamientos e insumos.
   * **Inventario:** gestiona el stock de medicamentos e insumos.
   * **Historial Clínico:** orientado a la lectura, consolida información longitudinal de cada mascota a partir de las atenciones y vistas en la base de datos.

7. **Base de datos (Oracle)**
   Componente central para la persistencia de datos. Soporta tanto las funciones serverless como los microservicios. Incluye vistas o materialized views que alimentan al servicio de Historial Clínico.

8. **Storage (Blob/objetos)**
   Servicio de almacenamiento serverless donde se guardan documentos, imágenes de mascotas, recetas y resultados de exámenes. Permite acceso mediante URLs presignadas y controladas por roles.




### Definiciones por componente

| Componente         | Tipo                       | Responsabilidad / Acción principal                                          | Entradas / Triggers                 | Datos que toca                          | Eventos que emite                             | Autorización (IDaaS)                        | Idempotencia                               | Observabilidad                       |                        |                       |
| ------------------ | -------------------------- | --------------------------------------------------------------------------- | ----------------------------------- | --------------------------------------- | --------------------------------------------- | ------------------------------------------- | ------------------------------------------ | ------------------------------------ | ---------------------- | --------------------- |
| **Frontend**       | App web/móvil              | UI, cache local, llama a BFF/FaaS vía Gateway                               | HTTP                                | N/A                                     | N/A                                           | Login OIDC/OAuth con IDaaS                  | N/A                                        | Trazas de UX (opcional)              |                        |                       |
| **IDaaS**          | Servicio gestionado        | Autenticación y emisión de tokens con roles/claims                          | Login/refresh                       | N/A                                     | N/A                                           | Autoridad de identidad para todo el backend | N/A                                        | Auditoría de acceso                  |                        |                       |
| **API Gateway**    | Servicio gestionado        | Entrada única: auth básica, rate limit, CORS, routing a BFF/FaaS            | HTTP/Webhook                        | N/A                                     | N/A                                           | Valida token, pasa claims                   | N/A                                        | Logs y métricas de llamadas          |                        |                       |
| **BFF**            | Microservicio IaaS         | Agregación/adaptación de lecturas para el Frontend                          | HTTP desde Gateway                  | Lecturas en DB/Storage/MS               | (Opc.) cache-invalidate                       | Verifica scopes/claims                      | N/A                                        | Logs y trazas                        |                        |                       |
| **FaaS Cliente**   | FaaS                       | CRUD de cliente (C-R-U-D)                                                   | HTTP                                | DB                                      | ClientCreated/Updated/Deleted                 | \`role=client                               | admin\`                                    | Sí (POST/PUT/DELETE)                 | Logs por invocación    |                       |
| **FaaS Mascota**   | FaaS                       | CRUD de mascota, vínculo con cliente                                        | HTTP                                | DB, Storage (fotos)                     | PetAdded/Updated/Removed                      | \`role=client                               | admin\`                                    | Sí                                   | Logs por invocación    |                       |
| **FaaS Reservas**  | FaaS                       | Crear, modificar, cancelar reserva                                          | HTTP                                | DB (reservas)                           | AppointmentBooked/Rescheduled/Cancelled       | \`role=client                               | admin\`                                    | **Crítica** (evitar dobles reservas) | Métricas y trazas      |                       |
| **MS Agenda**      | Microservicio IaaS         | Disponibilidad/ocupación, lecturas intensivas                               | HTTP desde BFF; eventos de Reservas | DB (slots/vistas)                       | AvailabilityUpdated                           | \`role=client                               | medic                                      | admin\`                              | No aplica              | Métricas SLA/latencia |
| **MS Atención**    | Microservicio IaaS         | Registrar acto clínico de la cita (anamnesis, diagnóstico, indicaciones)    | HTTP (BFF)                          | DB (consultas)                          | ConsultationRecorded/MedicationPrescribed     | \`role=medic                                | admin\`                                    | En escritura sí                      | Auditoría por registro |                       |
| **MS Inventario**  | Microservicio IaaS         | Catálogo/stock e insumos de atención                                        | HTTP (BFF)                          | DB                                      | StockAdjusted/LowStock                        | \`role=medic                                | admin\`                                    | En movimientos sí                    | KPIs consumo           |                       |
| **MS Historial**   | Microservicio IaaS (read)  | Historia longitudinal de la mascota (consume vistas/materializadas)         | HTTP (BFF)                          | **Vistas**/MV en DB; Storage (adjuntos) | (Opc.) ExportRequested                        | \`role=client                               | medic                                      | admin\` (row-level por mascota)      | No (solo lectura)      | Auditoría de acceso   |
| **FaaS Empleados** | FaaS                       | CRUD acotado (perfil, altas/bajas, ausencias)                               | HTTP                                | DB                                      | EmployeeCreated/ShiftPublished/ShiftCancelled | `role=admin` (self: `role=medic`)           | Sí                                         | Logs y métricas                      |                        |                       |
| **Integra Examen** | FaaS                       | Ingesta de resultados del laboratorio (webhook), guardar archivo y metadata | HTTP (webhook)                      | Storage (PDF/IMG), DB                   | LabResultImported                             | Auth por API Key/JWT; sin IDaaS             | **Crítica** (dedupe por `orderId/eventId`) | DLQ/errores por evento               |                        |                       |
| **Notificaciones** | FaaS                       | Enviar email/SMS/WhatsApp ante eventos (reserva, resultado, factura)        | Mensajes/eventos                    | N/A                                     | N/A                                           | Solo sistema                                | Sí (desduplicación)                        | Métricas de entrega                  |                        |                       |
| **Storage**        | Servicio gestionado (rojo) | Guardar adjuntos (fotos, PDFs, exámenes)                                    | Subidas directas o desde FaaS       | Objetos                                 | ObjectCreated (opcional)                      | URLs presignadas por rol                    | N/A                                        | Access logs                          |                        |                       |
| **DB Oracle**      | Base de datos              | Persistencia transaccional + vistas para Historial                          | SQL desde MS/FaaS                   | Tablas y **vistas/MV**                  | DB triggers opcionales                        | Row-level por `tenant/owner` (app-level)    | Upserts/locks                              | AWR/metrics                          |                        |                       |


---

### Políticas transversales (mini-checklist)

* **Autorización**: cada MS/FaaS valida **claims del IDaaS** (rol, scopes, sucursal) antes de operar.
* **Idempotencia** obligatoria: Reservas, Pagos, Ingesta de Exámenes, CRUD sensibles (usa `Idempotency-Key`/`orderId` + UPSERT).
* **Eventos**: todas las mutaciones relevantes publican eventos; Notificaciones y Agenda reaccionan. FaaS = **event-driven**.
* **Observabilidad**: logs por invocación, trazas distribuidas, métricas de tasa/latencia/errores; DLQ para webhooks.
* **Datos**: Historial **lee vistas**; adjuntos via Storage con **URLs presignadas** y expiración corta.

