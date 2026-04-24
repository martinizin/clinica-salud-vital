
# **1. Contexto del caso**


La clínica privada **SaludVital** cuenta con dos sistemas principales que apoyan su operación diaria:


  - **Sistema de Admisiones**, en el que se registran los pacientes que agendan o confirman una
atención.

  - **Sistema de Facturación**, que necesita recibir los pre-registros válidos para preparar la atención
administrativa y financiera.


Actualmente, ambos sistemas no están integrados en línea. El Sistema de Admisiones genera
periódicamente un archivo **CSV** con los pacientes pre-registrados del día. Ese archivo es colocado en una
carpeta compartida y luego el personal administrativo realiza manualmente varias tareas:


  - copiar el archivo a otra ubicación,

  - revisar si tiene errores,

  - conservar un respaldo,

  - procesarlo para facturación,

  - y eliminar manualmente el archivo original para evitar que vuelva a cargarse.


Con el crecimiento de la clínica, este proceso ha empezado a generar múltiples problemas:


  - algunos archivos se reprocesan por error,

  - otros se pierden o no quedan correctamente respaldados,

  - existen registros incompletos o con formato inválido,

  - facturación recibe datos inconsistentes,

  - no hay trazabilidad clara sobre qué archivos fueron procesados correctamente,

  - y el área de TI considera que el proceso actual depende demasiado de intervención manual.


La clínica solicita una primera solución de integración que automatice este flujo utilizando el estilo de
integración que hoy sí es viable para sus sistemas actuales: **transferencia de archivos** .


Sin embargo, la Dirección de TI también quiere que quede planteada una posible **evolución futura** : si el
Sistema de Admisiones pudiera más adelante exponer una API, desean conocer cómo se podría optimizar
parte de este proceso mediante una integración basada en servicios.


_© Darío Villamarín G._


ISWZ3104 – INTEGRACIÓN DE SISTEMAS


Usted ha sido asignado/a para:


1. analizar el problema actual,
2. proponer una solución de integración con Apache Camel basada en File Transfer,
3. implementar una versión funcional,
4. y diseñar/documentar una API futura que permita evolucionar el proceso.

# **2. Objetivo de la evaluación**


Esta evaluación tiene como propósito que el estudiante demuestre su capacidad para:


  - comprender un problema real de integración,

  - justificar técnicamente una estrategia de integración,

  - diseñar una solución basada en **File Transfer**,

  - implementarla utilizando **Apache Camel**,

  - y proponer una mejora futura mediante el diseño y documentación de una **API REST** .

# **3. Actividades a desarrollar**

## **PARTE 1. Comprensión y análisis del caso**

**Valor: 20 puntos**


A partir del caso presentado, responda de forma clara y técnica lo siguiente:


**1.** **Problema de integración**

Explique con sus palabras cuál es el problema principal de integración que tiene la clínica.


**2.** **Justificación del estilo**

Justifique por qué, en el contexto actual, el estilo **File Transfer** puede ser una solución razonable.


**3.** **Riesgos y limitaciones**

Identifique **al menos tres** limitaciones o riesgos de este estilo de integración en comparación con
una solución basada en APIs.

## **PARTE 2. Diseño de la solución File Transfer**

**Valor: 20 puntos**


Diseñe una solución de integración para el caso propuesto.


Su diseño debe indicar claramente:


  - carpetas involucradas,

  - flujo del archivo,

  - validaciones a aplicar,


_© Darío Villamarín G._


ISWZ3104 – INTEGRACIÓN DE SISTEMAS


  - criterio para determinar si un archivo es válido o inválido,

  - tratamiento de errores,

  - archivado del archivo procesado,

  - eliminación o prevención de reprocesamiento del archivo original,

  - y mecanismo básico de trazabilidad.


**Estructura sugerida de carpetas**
Puede trabajar con la siguiente estructura:


  - /data/input

  - /data/output

  - /data/archive

  - /data/error


**Puede representar su propuesta mediante un diagrama simple, esquema textual o explicación**
**estructurada.**

## **PARTE 3. Implementación con Apache Camel**

**Valor: 40 puntos**


Implemente una ruta en **Apache Camel** que procese archivos CSV de pacientes pre-registrados.


**Requerimientos funcionales mínimos**


La solución debe:


1. detectar archivos CSV ubicados en input,
2. validar su contenido,
3. enviar a output los archivos válidos,
4. archivar una copia del archivo procesado en archive,
5. guardar el archivo archivado con timestamp en el nombre,
6. enviar los archivos inválidos a error,
7. registrar logs o mensajes que permitan entender el resultado del procesamiento,
8. y evitar que el archivo quede disponible para ser reprocesado nuevamente desde input.


**Regla de archivado obligatoria**
El archivo archivado debe renombrarse usando timestamp.


**Ejemplo:**


pre_registros_2026-04-22_103015.csv


_© Darío Villamarín G._


ISWZ3104 – INTEGRACIÓN DE SISTEMAS


**Validaciones mínimas requeridas**


El archivo CSV debe tener estas columnas:


  - patient_id

  - full_name

  - appointment_date

  - insurance_code


Se considerará inválido si ocurre al menos una de las siguientes condiciones:


  - no tiene encabezado correcto,

  - alguna fila tiene campos vacíos,

  - appointment_date no cumple el formato YYYY-MM-DD,

  - insurance_code no pertenece a uno de estos valores: IESS, PRIVADO, NINGUNO.


**Evidencia técnica esperada**
En la implementación debe apreciarse, de forma directa o equivalente:


  - uso de file: como endpoint de entrada y salida,

  - uso de Camel para el flujo de procesamiento,

  - validación de contenido,

  - decisión condicional o enrutamiento según resultado,

  - logs básicos,

  - manejo de nombres de archivo.

## **PARTE 4. Evolución futura del proceso mediante API**

**Valor: 20 puntos**


Suponga que, en una segunda fase, el Sistema de Admisiones ya puede exponer una API.


La clínica quiere reducir la dependencia de archivos para ciertos procesos, especialmente para consultas
más rápidas, más seguras y con mejor trazabilidad.


Desarrolle lo siguiente:


**4.1 Propuesta de mejora**
Explique brevemente qué parte del proceso actual optimizaría con API y por qué.


_© Darío Villamarín G._


ISWZ3104 – INTEGRACIÓN DE SISTEMAS


**4.2 Diseño de API**
Diseñe una **API REST mínima** para esa evolución futura.


Debe proponer al menos:


  - un recurso principal,

  - endpoints,

  - método HTTP,

  - request/response,

  - estructura JSON,

  - códigos de estado.


**4.3 Documentación del contrato**
Documente esa API mediante una especificación breve en estilo **OpenAPI/Swagger** .


La especificación puede ser parcial, pero debe ser coherente y suficiente para comprender el contrato
propuesto.

# **4. Archivos y datos de apoyo**


Durante la evaluación, el docente entregará una carpeta base con los insumos necesarios para el
desarrollo del examen. Estos insumos podrán incluir:


  - archivo(s) CSV de ejemplo,


Cada estudiante deberá trabajar de manera individual sobre estos recursos.

# **5. Entregables**


Al finalizar la evaluación, cada estudiante deberá entregar:


1. Documento breve con:


a. análisis del caso,
b. justificación del estilo de integración,
c. diseño de la solución,
d. propuesta de evolución futura con API.


2. Código fuente de la solución en Apache Camel.
3. Evidencias de ejecución, por ejemplo:


_© Darío Villamarín G._


ISWZ3104 – INTEGRACIÓN DE SISTEMAS

a. capturas de pantalla,
b. logs,
c. resultados visibles del procesamiento.


4. Documento OpenAPI/Swagger de la API futura.

# **6. Criterios de evaluación**


La evaluación se calificará sobre 100 puntos, distribuidos de la siguiente manera:


  - **Parte 1. Análisis del caso:** 20 puntos

  - **Parte 2. Diseño de la solución:** 20 puntos

  - **Parte 3. Implementación Camel:** 40 puntos

  - **Parte 4. Evolución futura con API:** 20 puntos


Se valorará especialmente:


  - la comprensión correcta del problema,

  - la coherencia técnica de la solución propuesta,

  - el uso adecuado de Apache Camel,

  - la correcta validación y tratamiento de archivos,

  - la trazabilidad del flujo,

  - la calidad del diseño REST,

  - y la claridad de la documentación OpenAPI.

# **7. Consideraciones importantes**


- La evaluación es **individual** .

- No se permite copiar ni compartir soluciones.

- Se evaluará tanto el **resultado funcional** como la **justificación técnica** .

- Una solución parcialmente funcional pero correctamente razonada podrá recibir puntaje parcial.

- El código debe ser consistente con la solución planteada.

- El diseño de la API futura no requiere implementación completa, pero sí debe estar correctamente
planteado y documentado.


_© Darío Villamarín G._


ISWZ3104 – INTEGRACIÓN DE SISTEMAS

# **8. Recomendación de trabajo**

Administre su tiempo de forma adecuada. Se recomienda abordar la evaluación en este orden:


1. comprensión del caso,
2. diseño de la solución,
3. implementación Camel,
4. diseño y documentación de la API futura,
5. revisión final.


**Éxitos.**


_© Darío Villamarín G._


