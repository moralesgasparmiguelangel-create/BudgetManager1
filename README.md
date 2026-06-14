# BudgetManager

## Descripción del proyecto

BudgetManager es una aplicación móvil desarrollada en Android Studio que funciona como un gestor de presupuesto personal. Su objetivo principal es ayudar a los usuarios a registrar, organizar y controlar sus ingresos y gastos de una manera sencilla.

## Objetivo general

Desarrollar una aplicación Android funcional que permita al usuario administrar su presupuesto personal, aplicando buenas prácticas de programación, control de versiones con GitHub y trabajo colaborativo mediante Pull Requests.

## Objetivos específicos

- Registrar ingresos personales.
- Registrar gastos personales.
- Visualizar la información del presupuesto.
- Mantener el código organizado y legible.
- Trabajar en equipo usando GitHub Flow.

## Herramientas utilizadas

- Android Studio
- Git
- GitHub
- Visual Studio Code

## Flujo de trabajo utilizado

Para este proyecto utilizamos GitHub Flow.

Elegimos GitHub Flow porque es un flujo de trabajo sencillo y adecuado para proyectos escolares en equipo. La rama principal `main` se mantiene estable, mientras que cada integrante trabaja en una rama separada para realizar sus cambios.

Cada cambio importante se sube mediante una rama propia y después se integra a `main` usando un Pull Request. Esto permite que otro integrante revise el trabajo antes de unirlo al proyecto principal.

## Ramas utilizadas

- `main`: rama principal y estable del proyecto.
- `feature-readme`: rama usada para crear la documentación del proyecto.
- `feature-limpieza-codigo`: rama usada para mejorar nombres y organización del código.

## Instalación del proyecto

1. Clonar el repositorio:

```bash
git clone https://github.com/moralesgasparmiguelangel-create/BudgetManager1.git

```

## Guía de Operación: Registro del Primer Movimiento Financiero

Esta sección describe detalladamente el procedimiento paso a paso que el usuario debe seguir para inicializar su flujo financiero dentro de la aplicación, cubriendo tanto el registro de un ingreso como el de un gasto.

### Paso 1: Acceso a la Interfaz Principal

Inicie la aplicación e introduzca su clave de seguridad en la pantalla de acceso (LoginActivity).

Una vez autenticado, visualice la pantalla principal (MainActivity), donde encontrará el Balance Actual inicializado en $0.00, reflejando una base de datos limpia.

### Paso 2: Registrar el Primer Ingreso

Para añadir capital inicial o fondos a la aplicación, realice las siguientes acciones:

En la parte inferior de la pantalla, presione el botón + Registrar Ingreso (o + Ingreso). El sistema lo redireccionará automáticamente a la interfaz AgregarIngresoActivity.

Introducir el Monto: En el primer campo de texto, digite la cantidad numérica correspondiente al dinero recibido (por ejemplo: 15000.00).

Establecer el Concepto: En el segundo campo de texto, escriba una descripción breve que identifique el origen del dinero (por ejemplo: Pago de Quincena o Beca UV).

Seleccionar Categoría: Despliegue el menú dinámico (Spinner) y elija la categoría adecuada (por ejemplo: Sueldo u Otros).

Guardar Transacción: Presione el botón Registrar. El sistema procesará los datos, insertará de forma permanente el registro en la base de datos local a través de Room y mostrará un mensaje de confirmación (Toast): "Ingreso guardado en BD".

Al ser redirigido a la pantalla principal, observe cómo el Balance Actual y el panel de Ingresos se actualizan automáticamente con la cifra registrada.

### Paso 3: Registrar el Primer Gasto

Para deducir salidas de dinero del balance general, efectúe el siguiente procedimiento:

En la pantalla principal, presione el botón - Registrar Gasto (o - Gasto). Esto abrirá la interfaz AgregarGastoActivity.

Introducir el Monto: En el campo de monto, escriba el valor del gasto realizado (por ejemplo: 350.00).

Establecer el Concepto: Introduzca una descripción clara de la compra o pago (por ejemplo: Compra de despensa o Pasajes del mes).

Seleccionar Categoría: Despliegue el Spinner y determine la clasificación correspondiente (por ejemplo: Comida, Transporte u Ocio).

Guardar Transacción: Presione el botón Registrar. La aplicación registrará el movimiento con la fecha y hora exacta del sistema.

### Paso 4: Verificación en el Historial de Movimientos

Al finalizar ambos registros, la pantalla principal de la aplicación (MainActivity) actualizará sus componentes visuales de manera inmediata:

Balance Actual: Mostrará el resultado matemático neto de la operación (Ingresos - Gastos).

Resumen Estadístico: Los paneles superiores reflejarán los acumulados exactos de cada tipo de flujo.

Historial de Movimientos: El contenedor ListView renderizará de forma permanente y en un formato oscuro de alta legibilidad las dos celdas correspondientes a las transacciones recién creadas, ordenadas cronológicamente para el control del usuario.
