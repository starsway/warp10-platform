//
//   Copyright 2016  Cityzen Data
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp10.script.fwt.wavelets;

import io.warp10.script.fwt.Wavelet;

public class Wavelet_sym13 extends Wavelet {

  private static final int transformWavelength = 2;

  private static final double[] scalingDeComposition = new double[] { 6.820325263075319e-05, -3.573862364868901e-05, -0.0011360634389281183, -0.0001709428585302221, 0.0075262253899681, 0.005296359738725025, -0.02021676813338983, -0.017211642726299048, 0.013862497435849205, -0.0597506277179437, -0.12436246075153011, 0.19770481877117801, 0.6957391505614964, 0.6445643839011856, 0.11023022302137217, -0.14049009311363403, 0.008819757670420546, 0.09292603089913712, 0.017618296880653084, -0.020749686325515677, -0.0014924472742598532, 0.0056748537601224395, 0.00041326119884196064, -0.0007213643851362283, 3.690537342319624e-05, 7.042986690694402e-05,  };
  private static final double[] waveletDeComposition = new double[] { -7.042986690694402e-05, 3.690537342319624e-05, 0.0007213643851362283, 0.00041326119884196064, -0.0056748537601224395, -0.0014924472742598532, 0.020749686325515677, 0.017618296880653084, -0.09292603089913712, 0.008819757670420546, 0.14049009311363403, 0.11023022302137217, -0.6445643839011856, 0.6957391505614964, -0.19770481877117801, -0.12436246075153011, 0.0597506277179437, 0.013862497435849205, 0.017211642726299048, -0.02021676813338983, -0.005296359738725025, 0.0075262253899681, 0.0001709428585302221, -0.0011360634389281183, 3.573862364868901e-05, 6.820325263075319e-05,  };

  private static final double[] scalingReConstruction = new double[] { 7.042986690694402e-05, 3.690537342319624e-05, -0.0007213643851362283, 0.00041326119884196064, 0.0056748537601224395, -0.0014924472742598532, -0.020749686325515677, 0.017618296880653084, 0.09292603089913712, 0.008819757670420546, -0.14049009311363403, 0.11023022302137217, 0.6445643839011856, 0.6957391505614964, 0.19770481877117801, -0.12436246075153011, -0.0597506277179437, 0.013862497435849205, -0.017211642726299048, -0.02021676813338983, 0.005296359738725025, 0.0075262253899681, -0.0001709428585302221, -0.0011360634389281183, -3.573862364868901e-05, 6.820325263075319e-05,  };
  private static final double[] waveletReConstruction = new double[] { 6.820325263075319e-05, 3.573862364868901e-05, -0.0011360634389281183, 0.0001709428585302221, 0.0075262253899681, -0.005296359738725025, -0.02021676813338983, 0.017211642726299048, 0.013862497435849205, 0.0597506277179437, -0.12436246075153011, -0.19770481877117801, 0.6957391505614964, -0.6445643839011856, 0.11023022302137217, 0.14049009311363403, 0.008819757670420546, -0.09292603089913712, 0.017618296880653084, 0.020749686325515677, -0.0014924472742598532, -0.0056748537601224395, 0.00041326119884196064, 0.0007213643851362283, 3.690537342319624e-05, -7.042986690694402e-05,  };

  static {
    //
    // Reverse the arrays as we do convolutions
    //
    reverse(scalingDeComposition);
    reverse(waveletDeComposition);
  }

  private static final void reverse(double[] array) {
    int i = 0;
    int j = array.length - 1;
    
    while (i < j) {
      double tmp = array[i];
      array[i] = array[j];
      array[j] = tmp;
      i++;
      j--;
    }
  }

  public int getTransformWavelength() {
    return transformWavelength;
  }

  public int getMotherWavelength() {
    return waveletReConstruction.length;
  }

  public double[] getScalingDeComposition() {
    return scalingDeComposition;
  }

  public double[] getWaveletDeComposition() {
    return waveletDeComposition;
  }

  public double[] getScalingReConstruction() {
    return scalingReConstruction;
  }

  public double[] getWaveletReConstruction() {
    return waveletReConstruction;
  }
}
