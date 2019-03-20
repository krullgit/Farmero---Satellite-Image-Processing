const express = require('express')
const app = express()
const port = 3000


app.get('/', function(req, res) {


    //  START ---- get two NVDI images from Earth Engine and save them ---- START

    var coord = req.query.coord
    const spawn1 = require('child_process').spawn;
    const ls1 = spawn1('/opt/conda/envs/farmero_python3/bin/python', ['scripts/getimages.py', coord, 'arg2']);

    var back = ''

    ls1.stdout.on('data', (data) => {
        back = data
        console.log(back.toString())




         // START ---- open the NVDI Images, process the PCA on it and save it to a file called "changemap.jpg" ---- START

        const spawn2 = require('child_process').spawn;
        
        const ls2 = spawn2('/opt/conda/envs/farmero_python2/bin/python', ['scripts/computepca.py']);

        var back = ''

        ls2.stdout.on('data', (data) => {
            back = data
            console.log(back.toString())




            // START ---- calculate change points ---- START

            const spawn3 = require('child_process').spawn;
            const ls3 = spawn3('/opt/conda/envs/farmero_python3/bin/python', ['scripts/computepoints.py', coord, 'arg2']);
            var back = ''

            ls3.stdout.on('data', (data) => {
                back = data
                console.log(back.toString())
                res.download('data/points.txt')
            });
            ls3.stderr.on('data', (data) => {
                console.log(`stderr: ${data}`);
            });
            ls3.on('close', (code) => {
                console.log(`child process exited with code ${code}`);
            });

            // END ---- calculate change points ---- END





        });
        ls2.stderr.on('data', (data) => {
            console.log(`stderr: ${data}`);
        });
        ls2.on('close', (code) => {
            console.log(`child process exited with code ${code}`);
        });


        // END ---- open the NVDI Images, process the PCA on it and save it to a file called "changemap.jpg" ---- END




    });
    ls1.stderr.on('data', (data) => {
        console.log(`stderr: ${data}`);
    });
    ls1.on('close', (code) => {
        console.log(`child process exited with code ${code}`);
    });


    //  END ---- get two NVDI images from Earth Engine and save them ---- END


});

app.listen(port, () => console.log(`Example app listening on port ${port}!`))
